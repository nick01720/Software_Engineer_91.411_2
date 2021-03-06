/* 
 * Copyright (c) 2010 Jordan Frank, HumanSense Project, McGill University
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 * See LICENSE for more information 
 */
package ca.mcgill.hs.plugin;

import java.util.LinkedList;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import falldetection.analysis.fall.R;
import ca.mcgill.hs.prefs.PreferenceFactory;
import ca.mcgill.hs.util.Log;

/**
 * Logs observable bluetooth devices
 * 
 * @author Jordan Frank <jordan.frank@cs.mcgill.ca>
 */
public class BluetoothLogger extends InputPlugin {

	/**
	 * Receives Bluetooth discovery completion events.
	 * 
	 * @author Jordan Frank <jordan.frank@cs.mcgill.ca>
	 * 
	 */
	private class BluetoothDiscoveryListener extends BroadcastReceiver {

		public BluetoothDiscoveryListener() {
			super();
		}

		@Override
		public void onReceive(final Context c, final Intent intent) {
			c.unregisterReceiver(this);
			if (names.size() > 0 && addresses.size() > 0) {
				write(new BluetoothPacket(System.currentTimeMillis(),
						names.size(), names, addresses));
				names.clear();
				addresses.clear();
			}
			exec = getExecutionThread();
			exec.start();
			c.registerReceiver(this, new IntentFilter(
					BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		}
	}

	/**
	 * Receives Bluetooth scan completion events.
	 * 
	 * @author Jordan Frank <jordan.frank@cs.mcgill.ca>
	 * 
	 */
	private class BluetoothLoggerReceiver extends BroadcastReceiver {

		public BluetoothLoggerReceiver() {
			super();
		}

		@Override
		public void onReceive(final Context c, final Intent intent) {
			final BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			onDeviceFound(device);
		}
	}

	/**
	 * Contains a list of observed Bluetooth devices.
	 * 
	 * @author Jordan Frank <jordan.frank@cs.mcgill.ca>
	 * 
	 */
	public static class BluetoothPacket implements DataPacket {

		final long time;
		final int neighbours;
		final LinkedList<String> names;
		final LinkedList<String> addresses;

		public final static String PACKET_NAME = "BluetoothPacket";
		public final static int PACKET_ID = BluetoothPacket.PACKET_NAME
				.hashCode();

		public BluetoothPacket(final long time, final int neighbours,
				final LinkedList<String> names,
				final LinkedList<String> addresses) {
			this.time = time;
			this.neighbours = neighbours;
			this.names = names;
			this.addresses = addresses;
		}

		@SuppressWarnings("unchecked")
		@Override
		public DataPacket clone() {
			return new BluetoothPacket(time, neighbours,
					(LinkedList<String>) names.clone(),
					(LinkedList<String>) addresses.clone());
		}

		@Override
		public int getDataPacketId() {
			return BluetoothPacket.PACKET_ID;
		}

		@Override
		public String getInputPluginName() {
			return BluetoothLogger.PLUGIN_NAME;
		}

	}

	private static final String BLUETOOTH_DEFAULT_SCAN_INTERVAL = "60000";

	private static final String BLUETOOTH_THREAD_TAG = "BluetoothThread";
	private static final String BLUETOOTH_LOGGER_ENABLE_PREF = "bluetoothLoggerEnable";
	private static final String BLUETOOTH_LOGGER_TIME_INTERVAL_PREF = "bluetoothLoggerTimeInterval";
	private static final String FORCE_BLUETOOTH_ON_PREF = "forceBluetoothOn";

	final static String PLUGIN_NAME = "BluetoothLogger";

	final static int PLUGIN_ID = PLUGIN_NAME.hashCode();

	/**
	 * @see InputPlugin#getPreferences(PreferenceActivity)
	 */
	public static Preference[] getPreferences(final PreferenceActivity activity) {
		final Preference[] prefs = new Preference[3];

		prefs[0] = PreferenceFactory.getCheckBoxPreference(activity,
				BLUETOOTH_LOGGER_ENABLE_PREF,
				R.string.bluetoothlogger_enable_pref_label,
				R.string.bluetoothlogger_enable_pref_summary,
				R.string.bluetoothlogger_enable_pref_on,
				R.string.bluetoothlogger_enable_pref_off, true);

		prefs[1] = PreferenceFactory.getCheckBoxPreference(activity,
				FORCE_BLUETOOTH_ON_PREF,
				R.string.bluetoothlogger_autoenable_pref_label,
				R.string.bluetoothlogger_autoenable_pref_summary,
				R.string.bluetoothlogger_autoenable_pref_on,
				R.string.bluetoothlogger_autoenable_pref_off, true);

		prefs[2] = PreferenceFactory.getListPreference(activity,
				R.array.bluetoothlogger_pref_interval_strings,
				R.array.bluetoothlogger_pref_interval_values,
				BLUETOOTH_DEFAULT_SCAN_INTERVAL,
				BLUETOOTH_LOGGER_TIME_INTERVAL_PREF,
				R.string.bluetoothlogger_interval_pref,
				R.string.bluetoothlogger_interval_pref_summary);

		return prefs;
	}

	/**
	 * @see InputPlugin#hasPreferences()
	 */
	public static boolean hasPreferences() {
		return true;
	}

	/** The BluetoothAdapter used to start and stop discovery of devices. */
	private final BluetoothAdapter adapter;

	/** The interval of time between two subsequent scans. */
	private int timeBetweenDiscoveries;

	/** The Context in which the BluetoothLoggerReceiver will be registered. */
	private final Context context;

	/**
	 * The BluetoothLoggerReceiver from which we will get the bluetooth scan
	 * results.
	 */
	private BluetoothLoggerReceiver loggerReceiver;

	/**
	 * The BluetoothDiscoveryListener used to know when the discovery of
	 * bluetooth devices is completed.
	 */
	private BluetoothDiscoveryListener discoveryListener;

	/** List holding the names of the devices. */
	private final LinkedList<String> names = new LinkedList<String>();

	/** List holding the addresses of the devices. */
	private final LinkedList<String> addresses = new LinkedList<String>();

	/** Was the Bluetooth enable when the plugin was started. */
	private boolean wasEnabled = false;

	/**
	 * If this is true, the BluetoothThread is interrupted and an expected
	 * InterruptedException is caught.
	 */
	private boolean expectedInterrupt = false;

	/** The main BluetoothThread for this plugin. */
	private Thread exec;

	/**
	 * If true, the Bluetooth adapter will be automatically enabled when the
	 * service is started.
	 */
	private boolean forceBluetoothActivation;

	/**
	 * A boolean flag. If this is true, then the bluetooth adaptor is is the
	 * process of being enabled.
	 */
	private boolean isEnabling = false;

	final SharedPreferences prefs;

	/**
	 * Constructs a new bluetooth logging plugin. Enables the adapter if
	 * necessary.
	 * 
	 * @param context
	 *            The application context, necessary to get the preferences and
	 *            manage the bluetooth hardware.
	 */
	public BluetoothLogger(final Context context) {
		this.adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null) {
			if (adapter.isEnabled()) {
				wasEnabled = true;
			}
		}
		this.context = context;
		prefs = PreferenceFactory.getSharedPreferences(context);

		forceBluetoothActivation = prefs.getBoolean(FORCE_BLUETOOTH_ON_PREF,
				true);

		timeBetweenDiscoveries = Integer.parseInt(prefs.getString(
				BLUETOOTH_LOGGER_TIME_INTERVAL_PREF,
				BLUETOOTH_DEFAULT_SCAN_INTERVAL));
	}

	/**
	 * Returns a new thread that will serve as the execution thread for this
	 * plugin.
	 * 
	 * @return the execution thread for this plugin.
	 */
	private Thread getExecutionThread() {
		Log.i(BLUETOOTH_THREAD_TAG, "Starting execution thread");
		return new Thread() {
			@Override
			public void run() {
				try {
					sleep(timeBetweenDiscoveries);
					if (adapter != null) {
						if (!adapter.isEnabled()) {
							if (forceBluetoothActivation) {
								adapter.enable();
								isEnabling = true;
								Log.i(BLUETOOTH_THREAD_TAG,
										"Enabling Bluetooth Adapter");
							}
							while (!adapter.isEnabled()) {
							}
							isEnabling = false;
						}
						Log.i(BLUETOOTH_THREAD_TAG, "Starting discovery");
						adapter.startDiscovery();
					}
				} catch (final InterruptedException e) {
					if (expectedInterrupt) {
						Log.e(BLUETOOTH_THREAD_TAG,
								"Expected thread interruption");
					} else {
						Log.e(BLUETOOTH_THREAD_TAG, e);
					}
				}
			}
		};
	}

	/**
	 * Called when a device is found. Adds the name and address of the found
	 * device to the lists of names/addresses found during this scan.
	 * 
	 * @param device
	 *            The BluetoothDevice that was found.
	 */
	private void onDeviceFound(final BluetoothDevice device) {
		if (device.getName() == null) {
			return;
		}
		names.add(device.getName());
		addresses.add(device.getAddress());
	}

	@Override
	protected void onPluginStart() {
		pluginEnabled = prefs.getBoolean(BLUETOOTH_LOGGER_ENABLE_PREF, true);
		if (!pluginEnabled) {
			return;
		}
		if (adapter == null) {
			return; // Device does not support Bluetooth
		}

		loggerReceiver = new BluetoothLoggerReceiver();
		context.registerReceiver(loggerReceiver, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
		Log.i(PLUGIN_NAME, "Registered logger receiver.");

		discoveryListener = new BluetoothDiscoveryListener();
		context.registerReceiver(discoveryListener, new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		Log.i(PLUGIN_NAME, "Registered discovery listener.");

		exec = getExecutionThread();
		exec.start();
	}

	@Override
	protected void onPluginStop() {
		/*
		 * Interrupts the execution thread, unregisters all broadcast receivers
		 * and cancels any ongoing discoveries. Disables Bluetooth adapter if it
		 * was disabled when the service was started.
		 */
		if (!pluginEnabled) {
			return;
		}
		if (adapter == null) {
			return;
		}

		expectedInterrupt = true;
		exec.interrupt();

		try {
			context.unregisterReceiver(loggerReceiver);
		} catch (final IllegalArgumentException e) {
			Log.e(PLUGIN_NAME,
					"Exception thrown unregistering loggerReceiver. Ignoring.");
			Log.e(PLUGIN_NAME, e);
		}
		Log.i(PLUGIN_NAME, "Unegistered loggerReceiver");
		try {
			context.unregisterReceiver(discoveryListener);
		} catch (final IllegalArgumentException e) {
			Log.e(PLUGIN_NAME,
					"Exception thrown unregistering discoveryListener. Ignoring.");
			Log.e(PLUGIN_NAME, e);
		}
		Log.i(PLUGIN_NAME, "Unegistered discoveryListener");

		adapter.cancelDiscovery();

		if (!wasEnabled) {
			if (isEnabling) {
				final BroadcastReceiver disabler = new BroadcastReceiver() {
					@Override
					public void onReceive(final Context context,
							final Intent intent) {
						if (intent.getAction().equals(
								BluetoothAdapter.ACTION_STATE_CHANGED)) {
							adapter.disable();
						}
					}
				};
				context.registerReceiver(disabler, new IntentFilter(
						BluetoothAdapter.ACTION_STATE_CHANGED));
			} else {
				adapter.disable();
			}
		}
	}

	@Override
	public void onPreferenceChanged() {
		timeBetweenDiscoveries = Integer.parseInt(prefs.getString(
				BLUETOOTH_LOGGER_TIME_INTERVAL_PREF,
				BLUETOOTH_DEFAULT_SCAN_INTERVAL));

		forceBluetoothActivation = prefs.getBoolean(FORCE_BLUETOOTH_ON_PREF,
				true);

		final boolean pluginEnabledNew = prefs.getBoolean(
				BLUETOOTH_LOGGER_ENABLE_PREF, true);
		super.changePluginEnabledStatus(pluginEnabledNew);
	}
}
