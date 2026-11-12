using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using distinguish.Model;
using distinguish.ViewModel;

namespace distinguish.Common;

internal class Global
{
	public static string iniConfigPath = "./appsettings.ini";

	public static LoginUser loginUser;

	public static string jwt = "";

	public static string ipAddr = "";

	public static string username = "";

	public static string password = "";

	public static List<Device> devices;

	public static CryptionUtils.RSAKEY rsaKey;

	public static string dataKey = "";

	public static bool serverInputVis = true;

	public static string app_version = "2.0.14";

	public static string app_name = "xcs";

	public static HomeViewModel HomeViewModel;

	public static bool verify = false;

	public static string verifyDefaultPwd = "E951081AC30FFA83F10B68CDE1CAC7";

	public static void RemoveCacheImg()
	{
		DirectoryInfo directoryInfo = new DirectoryInfo(AppDomain.CurrentDomain.BaseDirectory + "cacheImg\\");
		if (directoryInfo.Exists)
		{
			try
			{
				directoryInfo.Delete(recursive: true);
			}
			catch (Exception)
			{
			}
		}
	}

	public static string FullImgUrl(string url)
	{
		if (url.Contains("http://") || url.Contains("https://"))
		{
			return url;
		}
		return "http://" + ipAddr + "/" + url;
	}

	public static string GetDataBash64(string bash64)
	{
		string text = bash64;
		string[] array = dataKey.Split(new char[1] { '|' });
		for (int i = 0; i < array.Length; i++)
		{
			string[] array2 = array[i].Split(new char[1] { ',' });
			text = text.Replace(array2[1], array2[0]);
		}
		return text;
	}

	public static string GetMd5(string cl)
	{
		string text = "";
		byte[] array = MD5.Create().ComputeHash(Encoding.UTF8.GetBytes(cl));
		for (int i = 0; i < array.Length; i++)
		{
			text += array[i].ToString("X");
		}
		return text;
	}
}
