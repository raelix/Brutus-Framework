package com.brutus.andbrutus.utils;

public interface Rest {
	public static final int pollingRefresh = 1000;
	public static final int notificationAlarmId = 1221;
	public static final int notificationTitleId = 1331;
	public static final int pollingStart = 0;
	public static final int listAlarmItemSize = 80;
	public static final  String dbName ="database";
	public static final String configParameters = "parameters";
	public static final String listParameters = "parameters";
	public static final String prefix = "http://";
	public static final String read = "/brutus/read";
	public static final String write = "/brutus/write";
	public static final String status = "/brutus/status";
	public static final String configuration = "/brutus/configuration";
	public static final String plugin = "/brutus/read";
	public static final String idKey = "/brutus/idkey/";
	public static final String alarm = "/brutus/alarm";
	public static final String resetAlarm = "/brutus/resetalarm";
	public static final String _camera = "_camera";
	public static final String _cameraEnable = "_cameraEnable";
	public static final String _readOnly = "_rw";
	public static final String _disable = "_disable";
	public static final String _log = "_log";
	public static final String _alarm = "_alarm";
	public static final String _type = "_type";
	public static final String _unit = "_unit";
	public static final String _percent = "_percent";
	public static final String _open = "_open";
	public static final String _openCamera = "_openCamera";
	public static final String _limitMin = "_limitMin";
	public static final String _limitMax = "_limitMax";
	public static final String _alarmMin = "_alarmMin";
	public static final String _repeate = "_repeate";
	public static final String _alarmMax = "_alarmMax";
	public static final int defaults = 0;
	public static final int gauge = 1;
	public static final int button = 2;
	public static final int input = 3;
	public static final int graph = 4;
	public static final int camera = 5;
	public static final String defaultString = "default";
	public static final String gaugeString = "gauge";
	public static final String buttonString = "button";
	public static final String inputString = "input";
	public static final String graphString = "graph";
	public static final String cameraString = "camera";
	//Alarm request
	public static final  String minAlarm = "minAlarm";
	public static final  String maxAlarm = "maxAlarm";
	public static final  String repeat = "repeat";
	public static final  String enable = "enable";
	//Notification Request
	public static final String command = "command";
	public static final String disable = "disable";
	public static final String reset = "reset";


}
