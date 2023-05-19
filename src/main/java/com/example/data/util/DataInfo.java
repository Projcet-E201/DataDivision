package com.example.data.util;

import java.util.concurrent.TimeUnit;

public class DataInfo {

	private static final int DUPLICATE = 20;
	private static final int CLIENT_COUNT = 11;

	// Motor 정보
	public static final int MOTOR_COUNT = 10;
	public static final int MOTOR_BATCH_SIZE = CLIENT_COUNT * MOTOR_COUNT;
	public static final int MOTOR_CALCULATE_TIME = DUPLICATE * 5;
	public static final TimeUnit MOTOR_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// Vacuum 정보
	public static final int VACUUM_COUNT = 30;
	public static final int VACUUM_BATCH_SIZE = CLIENT_COUNT * VACUUM_COUNT;
	public static final int VACUUM_CALCULATE_TIME = DUPLICATE * 10;
	public static final TimeUnit VACUUM_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// AirOutKpaConsumer 정보
	public static final int AIR_OUT_KPA_COUNT = 5;
	public static final int AIR_OUT_KPA_BATCH_SIZE = CLIENT_COUNT * AIR_OUT_KPA_COUNT;
	public static final int AIR_OUT_KPA_CALCULATE_TIME = DUPLICATE * 30;
	public static final TimeUnit AIR_OUT_KPA_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// AirOutMpaConsumer 정보
	public static final int AIR_OUT_MPA_COUNT = 5;
	public static final int AIR_OUT_MPA_BATCH_SIZE = CLIENT_COUNT * AIR_OUT_MPA_COUNT;
	public static final int AIR_OUT_MPA_CALCULATE_TIME = DUPLICATE * 10;
	public static final TimeUnit AIR_OUT_MPA_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// AirInKpaConsumer 정보
	public static final int AIR_IN_KPA_COUNT = 10;
	public static final int AIR_IN_KPA_BATCH_SIZE = CLIENT_COUNT * AIR_IN_KPA_COUNT;
	public static final int AIR_IN_KPA_CALCULATE_TIME = DUPLICATE * 1;
	public static final TimeUnit AIR_IN_KPA_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// Water 정보
	public static final int WATER_COUNT = 10;
	public static final int WATER_BATCH_SIZE = CLIENT_COUNT * WATER_COUNT;
	public static final int WATER_CALCULATE_TIME = DUPLICATE * 10;
	public static final TimeUnit WATER_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// AbrasionConsumer (마모량) 정보
	public static final int ABRASION_COUNT = 5;
	public static final int ABRASION_BATCH_SIZE = CLIENT_COUNT * ABRASION_COUNT;
	public static final int ABRASION_CALCULATE_TIME = DUPLICATE * 1;
	public static final TimeUnit ABRASION_CALCULATE_TIME_UNIT = TimeUnit.MINUTES;

	// 부하량 정보
	public static final int LOAD_COUNT = 5;
	public static final int LOAD_BATCH_SIZE = CLIENT_COUNT * LOAD_COUNT;
	public static final int LOAD_CALCULATE_TIME = DUPLICATE * 1;
	public static final TimeUnit LOAD_CALCULATE_TIME_UNIT = TimeUnit.MINUTES;

	// VelocityConsumer (회전속도) 정보
	public static final int VELOCITY_COUNT = 5;
	public static final int VELOCITY_BATCH_SIZE = CLIENT_COUNT * VELOCITY_COUNT;
	public static final int VELOCITY_CALCULATE_TIME = DUPLICATE * 1;
	public static final TimeUnit VELOCITY_CALCULATE_TIME_UNIT = TimeUnit.MINUTES;


	// Analog 정보
	public static final int ANALOG_COUNT = 1;
	public static final int ANALOG_CALCULATE_TIME = 30;
	public static final TimeUnit ANALOG_CALCULATE_TIME_UNIT = TimeUnit.MINUTES;


	// Image
	public static final int IMAGE_COUNT = 1;
	public static final int IMAGE_CALCULATE_TIME = 1;
	public static final TimeUnit IMAGE_CALCULATE_TIME_UNIT = TimeUnit.HOURS;


	// MachineState 정보
	public static final int MACHINE_STATE_COUNT = 1;


	public static int getDataCountByType(DataType dataType) {
		switch (dataType) {
			case MACHINE_STATE:
				return MACHINE_STATE_COUNT;
			case MOTOR:
				return MOTOR_COUNT;
			case VACUUM:
				return VACUUM_COUNT;
			case AIR_OUT_KPA:
				return AIR_OUT_KPA_COUNT;
			case AIR_OUT_MPA:
				return AIR_OUT_MPA_COUNT;
			case AIR_IN_KPA:
				return AIR_IN_KPA_COUNT;
			case WATER:
				return WATER_COUNT;
			case ABRASION:
				return ABRASION_COUNT;
			case LOAD:
				return LOAD_COUNT;
			case VELOCITY:
				return VELOCITY_COUNT;
			case ANALOG:
				return ANALOG_COUNT;
			case IMAGE:
				return IMAGE_COUNT;

			default:
				throw new IllegalArgumentException("Unknown data type: " + dataType);
		}
	}
}
