/* 
 * Copyright (c) 2010 Jordan Frank, HumanSense Project, McGill University
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 * See LICENSE for more information 
 */
package ca.mcgill.hs.plugin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import falldetection.analysis.fall.R;
import ca.mcgill.hs.plugin.BluetoothLogger.BluetoothPacket;
import ca.mcgill.hs.plugin.GPSLogger.GPSPacket;
import ca.mcgill.hs.plugin.GSMLogger.GSMPacket;
import ca.mcgill.hs.plugin.WifiLogger.WifiPacket;
import ca.mcgill.hs.prefs.PreferenceFactory;
import ca.mcgill.hs.util.Log;

/**
 * This output plugin takes data from a ReadableByteChannel and outputs it to
 * the Android's logcat.
 * 
 * @author Jordan Frank, Cicerone Cojocaru, Jonathan Pitre
 */
public final class ScreenOutput extends OutputPlugin {

	private static final String SCREEN_OUTPUT_ENABLE_PREF = "screenOutputEnable";
	private static final String PLUGIN_NAME = "ScreenOutput";

	/**
	 * Parses and writes given BluetoothPacket to the Android's logcat.
	 * 
	 * @param packet
	 *            the BluetoothPacket to parse and write out.
	 */
	private static void dataParse(final BluetoothPacket packet) {
		Log.i(PLUGIN_NAME, "Bluetooth Device Found");
		Log.i(PLUGIN_NAME, "Name : " + packet.names.toString());
		Log.i(PLUGIN_NAME, "Address : " + packet.addresses.toString());
	}

	/**
	 * Parses and writes given GPSLoggerPacket to the Android's logcat.
	 * 
	 * @param packet
	 *            the GPSLoggerPacket to parse and write out.
	 */
	private static void dataParse(final GPSPacket packet) {
		Log.i(PLUGIN_NAME, "Area: [" + packet.altitude + "][" + packet.latitude
				+ "][" + packet.longitude + "]");
	}

	/**
	 * Parses and writes given GSMLoggerPacket to the Android's logcat.
	 * 
	 * @param packet
	 *            the GSMLoggerPacket to parse and write out.
	 */
	private static void dataParse(final GSMPacket packet) {
		Log.i(PLUGIN_NAME, "Timestamp : " + packet.time);
		Log.i(PLUGIN_NAME, "MCC : " + packet.mcc);
		Log.i(PLUGIN_NAME, "MNC : " + packet.mnc);
		Log.i(PLUGIN_NAME, "CID : " + packet.cid);
		Log.i(PLUGIN_NAME, "LAC : " + packet.lac);
		Log.i(PLUGIN_NAME, "RSSI : " + packet.rssi);
		Log.i(PLUGIN_NAME, "Neighbors : " + packet.neighbors);
		for (int i = packet.neighbors - 1; i >= 0; i--) {
			Log.i(PLUGIN_NAME, "Neighbor " + i + " CID : " + packet.cids[i]);
			Log.i(PLUGIN_NAME, "Neighbor " + i + " LAC : " + packet.lacs[i]);
			Log.i(PLUGIN_NAME, "Neighbor " + i + " RSSI : " + packet.rssis[i]);
		}
	}

	/**
	 * Parses and writes given WifiLoggerPacket to the Android's logcat.
	 * 
	 * @param packet
	 *            the WifiLoggerPacket to parse and write out.
	 */
	private static void dataParse(final WifiPacket packet) {
		Log.i(PLUGIN_NAME, "Time: " + packet.timestamp);
		Log.i(PLUGIN_NAME, "Neighbors: " + packet.numAccessPoints);
		final int j = packet.signalStrengths.length;
		for (int i = 0; i < j; i++) {
			Log.i(PLUGIN_NAME, "SSID: " + packet.SSIDs[i]);
			Log.i(PLUGIN_NAME, "Level: " + packet.signalStrengths[i]);
			Log.i(PLUGIN_NAME, "BSSID: " + packet.BSSIDs[i]);
			Log.i(PLUGIN_NAME, " ");
		}
	}

	/**
	 * @see InputPlugin#getPreferences(PreferenceActivity)
	 */
	public static Preference[] getPreferences(final PreferenceActivity activity) {
		final Preference[] prefs = new Preference[1];

		prefs[0] = PreferenceFactory.getCheckBoxPreference(activity,
				SCREEN_OUTPUT_ENABLE_PREF,
				R.string.screenoutput_enable_pref_label,
				R.string.screenoutput_enable_pref_summary,
				R.string.screenoutput_enable_pref_on,
				R.string.screenoutput_enable_pref_off, false);

		return prefs;
	}

	/**
	 * @see OutputPlugin#hasPreferences()
	 */
	public static boolean hasPreferences() {
		return true;
	}

	private final SharedPreferences prefs;

	/**
	 * This is the basic constructor for the ScreenOutput plugin. It has to be
	 * instantiated before it is started, and needs to be passed a reference to
	 * a Context.
	 * 
	 * @param context
	 *            the context in which this plugin is created.
	 */
	public ScreenOutput(final Context context) {
		prefs = PreferenceFactory.getSharedPreferences(context);
	}

	/**
	 * This method gets called whenever an InputPlugin registered to
	 * ScreenOutput has data available to output. This method calls the
	 * appropriate version of dataParse based on the DataPacket type.
	 * 
	 * @param packet
	 *            the DataPacket recieved.
	 */
	@Override
	void onDataReceived(final DataPacket packet) {
		if (!pluginEnabled) {
			return;
		}
		final int id = packet.getDataPacketId();
		if (id == WifiPacket.PACKET_ID) {
			dataParse((WifiPacket) packet);
		} else if (id == GPSPacket.PACKET_ID) {
			dataParse((GPSPacket) packet);
		} else if (id == GSMPacket.PACKET_ID) {
			dataParse((GSMPacket) packet);
		} else if (id == BluetoothPacket.PACKET_ID) {
			dataParse((BluetoothPacket) packet);
		}
	}

	@Override
	protected void onPluginStart() {
		pluginEnabled = prefs.getBoolean(SCREEN_OUTPUT_ENABLE_PREF, false);
	}

	@Override
	protected void onPluginStop() {
	}

	@Override
	public void onPreferenceChanged() {
		final boolean pluginEnabledNew = prefs.getBoolean(
				SCREEN_OUTPUT_ENABLE_PREF, false);
		super.changePluginEnabledStatus(pluginEnabledNew);
	}
}
