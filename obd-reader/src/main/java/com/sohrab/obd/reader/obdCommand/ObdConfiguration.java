package com.sohrab.obd.reader.obdCommand;

import android.content.Context;
import android.widget.Toast;

import com.sohrab.obd.reader.obdCommand.control.DistanceMILOnCommand;
import com.sohrab.obd.reader.obdCommand.control.DistanceSinceCCCommand;
import com.sohrab.obd.reader.obdCommand.control.DtcNumberCommand;
import com.sohrab.obd.reader.obdCommand.control.EquivalentRatioCommand;
import com.sohrab.obd.reader.obdCommand.control.IgnitionMonitorCommand;
import com.sohrab.obd.reader.obdCommand.control.ModuleVoltageCommand;
import com.sohrab.obd.reader.obdCommand.control.PendingTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.PermanentTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.TimingAdvanceCommand;
import com.sohrab.obd.reader.obdCommand.control.TroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.VinCommand;
import com.sohrab.obd.reader.obdCommand.engine.AbsoluteLoadCommand;
import com.sohrab.obd.reader.obdCommand.engine.LoadCommand;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.engine.RuntimeCommand;
import com.sohrab.obd.reader.obdCommand.engine.ThrottlePositionCommand;
import com.sohrab.obd.reader.obdCommand.fuel.AirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.fuel.ConsumptionRateCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FindFuelTypeCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelTrimCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.pressure.BarometricPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.DescribeProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.DescribeProtocolNumberCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AmbientAirTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;

import java.util.ArrayList;

/**
 * Created by "Sohrab" on 1/9/2018.
 */

public class ObdConfiguration {

    private static ArrayList<ObdCommand> mObdCommands;

    public static ArrayList<ObdCommand> getmObdCommands() {
        if (mObdCommands == null)
            getDefaultObdCommand();
        return mObdCommands;
    }

    public static void setmObdCommands(Context context, ArrayList<ObdCommand> obdCommands) {

        if (mObdCommands == null) {
            mObdCommands = obdCommands;
            return;
        }

        Toast.makeText(context, "you can not add command after ObdReaderService started!", Toast.LENGTH_SHORT).show();
    }

    private static void getDefaultObdCommand() {
        mObdCommands = new ArrayList<>();

        mObdCommands.add(new SpeedCommand());
        mObdCommands.add(new RPMCommand());
        mObdCommands.add(new RuntimeCommand());
        mObdCommands.add(new MassAirFlowCommand());

        //Pressure
        mObdCommands.add(new IntakeManifoldPressureCommand());
        mObdCommands.add(new BarometricPressureCommand());
        mObdCommands.add(new FuelPressureCommand());
        mObdCommands.add(new FuelRailPressureCommand());

        //Tempereture
        mObdCommands.add(new AirIntakeTemperatureCommand());
        mObdCommands.add(new AmbientAirTemperatureCommand());
        mObdCommands.add(new EngineCoolantTemperatureCommand());
        mObdCommands.add(new OilTempCommand());

        //Fuel
        mObdCommands.add(new FindFuelTypeCommand());
        mObdCommands.add(new ConsumptionRateCommand());
        mObdCommands.add(new FuelLevelCommand());
        mObdCommands.add(new FuelTrimCommand());
        mObdCommands.add(new AirFuelRatioCommand());
        mObdCommands.add(new WidebandAirFuelRatioCommand());

        //control
        mObdCommands.add(new DistanceMILOnCommand());
        mObdCommands.add(new DistanceSinceCCCommand());
        mObdCommands.add(new DtcNumberCommand());
        mObdCommands.add(new EquivalentRatioCommand());
        mObdCommands.add(new IgnitionMonitorCommand());
        mObdCommands.add(new ModuleVoltageCommand());
        mObdCommands.add(new TimingAdvanceCommand());
        mObdCommands.add(new VinCommand());
        //Trouble codes
        mObdCommands.add(new TroubleCodesCommand());
        mObdCommands.add(new PermanentTroubleCodesCommand());
        mObdCommands.add(new PendingTroubleCodesCommand());


        //engine
        mObdCommands.add(new AbsoluteLoadCommand());
        mObdCommands.add(new LoadCommand());
        mObdCommands.add(new OilTempCommand());
        mObdCommands.add(new ThrottlePositionCommand());

        //protocol
        mObdCommands.add(new DescribeProtocolCommand());
        mObdCommands.add(new DescribeProtocolNumberCommand());


    }
}
