namespace distinguish.Common;

internal class AppConfig
{
	public static string getConfig(string root, string path)
	{
		return new FilesINI().INIRead(root, path, Global.iniConfigPath);
	}

	public static void setConfig(string root, string path, string val)
	{
		new FilesINI().INIWrite(root, path, val, Global.iniConfigPath);
	}
}
