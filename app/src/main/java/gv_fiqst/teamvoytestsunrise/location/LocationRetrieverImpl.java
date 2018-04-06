package gv_fiqst.teamvoytestsunrise.location;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;

class LocationRetrieverImpl implements LocationRetriever {
    private static LocationRetrieverImpl sImpl;

    public static LocationRetrieverImpl get() {
        return sImpl;
    }

    public static void init(Context context) {
        if (sImpl != null) {
            throw new IllegalStateException("RetrofitManager is already initialized");
        }

        sImpl = new LocationRetrieverImpl(context);
    }

    private final AtomicInteger mStartRequests = new AtomicInteger(0);
    private FusedLocationProviderClient mClient;
    private LocationRequest mRequest;

    private List<Listener> mListeners;

    private Location mLastLocation;
    private Geocoder mGeocoder;

    public LocationRetrieverImpl(Context context) {
        mClient = LocationServices.getFusedLocationProviderClient(context);
        mRequest = LocationRequest.create()
                .setInterval(30000)         // 30 sec
                .setFastestInterval(5000)   // 5 sec
                .setPriority(LocationRequest.PRIORITY_LOW_POWER);

        mListeners = new ArrayList<>();
        mGeocoder = new Geocoder(context);
    }

    @Override
    public void addLocationListener(Listener l) {
        mListeners.add(l);
    }

    @Override
    public void removeLocationListener(Listener l) {
        mListeners.remove(l);

        if (mListeners.size() == 0) {
            forceStop();
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void start() {
        int val = mStartRequests.incrementAndGet();

        if (val == 1) {
            mClient.requestLocationUpdates(
                mRequest, mCallback, Looper.getMainLooper()
            );
        }
    }

    @Override
    public void stop() {
        int val = mStartRequests.decrementAndGet();

        if (val <= 0) {
            forceStop();
        }
    }

    @Override
    public boolean serviceIsAvailable() {
        return Geocoder.isPresent();
    }

    private void forceStop() {
        mStartRequests.set(0);

        mClient.removeLocationUpdates(mCallback);
    }

    private void onNewLocation(Location loc) {
        synchronized (this) {
            if (loc != null) {
                if (mLastLocation != null
                        && mLastLocation.distanceTo(loc) <= 10000) { // 10km
                    return;
                }

                mLastLocation = loc;
                LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                LocationInfo.Builder builder = new LocationInfo.Builder()
                        .setCoords(latLng)
                        .setAccuracy(loc.getAccuracy());

                try {
                    List<Address> list = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    fireListeners(
                            builder.setCityName(
                                    list.get(0).getLocality()
                            ).build()
                    );
                } catch (Exception e) {
                    mLastLocation = null;
                }
            }
        }
    }

    private void fireListeners(LocationInfo info) {
        // new ArrayList<>(mListeners) for ConcurrentModificationException when we try to modify
        // list while iterating over it.
        for (Listener l : new ArrayList<>(mListeners)) {
            l.onLocationUpdate(info);
        }
    }

    private final LocationCallback mCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            onNewLocation(locationResult.getLastLocation());
        }
    };
}
