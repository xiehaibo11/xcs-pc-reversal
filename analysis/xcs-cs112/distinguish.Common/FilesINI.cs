using System.IO;
using System.Runtime.InteropServices;
using System.Text;

namespace distinguish.Common;

internal class FilesINI
{
	[DllImport("kernel32")]
	private static extern long WritePrivateProfileString(string section, string key, string val, string filePath);

	[DllImport("kernel32")]
	private static extern int GetPrivateProfileString(string section, string key, string def, StringBuilder retVal, int size, string filePath);

	public void INIWrite(string section, string key, string value, string path)
	{
		WritePrivateProfileString(section, key, value, path);
	}

	public string INIRead(string section, string key, string path)
	{
		StringBuilder stringBuilder = new StringBuilder(255);
		GetPrivateProfileString(section, key, "", stringBuilder, 255, path);
		return stringBuilder.ToString();
	}

	public void INIDelete(string FilePath)
	{
		File.Delete(FilePath);
	}
}
