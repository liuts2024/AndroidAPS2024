package info.nightscout.androidaps.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.nightscout.androidaps.interfaces.PumpInterface;
import info.nightscout.androidaps.logging.L;
import info.nightscout.androidaps.plugins.configBuilder.ConfigBuilderPlugin;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.RileyLinkConst;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.RileyLinkUtil;

public class BluetoothStateReceiver extends BroadcastReceiver {

    private static Logger LOG = LoggerFactory.getLogger(L.PUMP);

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        PumpInterface activePump = ConfigBuilderPlugin.getPlugin().getActivePump();

        LOG.debug("BluetoothStateReceiver");

        if (action != null && activePump != null) {

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                case BluetoothAdapter.STATE_TURNING_OFF:
                case BluetoothAdapter.STATE_TURNING_ON:
                    break;

                case BluetoothAdapter.STATE_ON: {
                    if ("Medtronic".equals(activePump.manufacter())) {
                        LOG.debug("BluetoothStateReceiver: Bluetooth back on. Sending broadcast to RileyLink Framework");
                        RileyLinkUtil.sendBroadcastMessage(RileyLinkConst.Intents.BluetoothReconnected);
                    }
                }
                break;
            }
        }
    }


}