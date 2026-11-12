namespace distinguish.Common;

internal class Constant
{
	public static int PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_KEEPALIVE = 1;

	public static int PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_COMMON_DATA = 2;

	public static int PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_LOGOUT = 3;

	public static int PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_RECIVED = 4;

	public static int PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_ECHO = 5;

	public static int PROTOCOL_TYPE_C_FROM_CLIENT_TYPE_OF_LOGIN = 6;

	public static int PROTOCAL_TYPE_S_FROM_SERVER_TYPE_OF_RESPONSE_LOGIN = 50;

	public static int PROTOCAL_TYPE_S_FROM_SERVER_TYPE_OF_RESPONSE_KEEP_ALIVE = 51;

	public static int PROTOCAL_TYPE_S_FROM_SERVER_TYPE_OF_RESPONSE_FOR_ERROR = 52;

	public static int PROTOCAL_TYPE_S_FROM_SERVER_TYPE_OF_RESPONSE_ECHO = 53;

	public static int PROTOCAL_TYPE_S_FROM_SERVER_TYPE_OF_KICKOUT = 54;

	public static int ERROR_CODE_RESPONSE_FOR_UN_LOGIN = 301;

	public static string INI_APP_ROOT = "APP";

	public static string INI_APP_SERVER = "SERVER";

	public static string INI_APP_USERNAME = "USERNAME";

	public static string INI_APP_PASSWORD = "PASSWORD";

	public static string INI_VERIFY_ROOT = "VERIFY";

	public static string INI_VERIFY_PWD = "PWD";
}
