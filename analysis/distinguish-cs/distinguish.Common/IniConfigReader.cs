using System;
using Microsoft.Extensions.Configuration;

namespace distinguish.Common;

internal class IniConfigReader
{
	public static IConfigurationRoot ReadIniFile(string filePath)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Expected O, but got Unknown
		try
		{
			ConfigurationBuilder val = new ConfigurationBuilder();
			IniConfigurationExtensions.AddIniFile((IConfigurationBuilder)val, filePath);
			return val.Build();
		}
		catch (Exception ex)
		{
			Console.WriteLine("读取INI文件失败: " + ex.Message);
			return null;
		}
	}
}
