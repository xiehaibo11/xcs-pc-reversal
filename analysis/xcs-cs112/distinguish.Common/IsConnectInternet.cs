using System;
using System.Runtime.InteropServices;

namespace distinguish.Common;

internal class IsConnectInternet
{
	[DllImport("wininet.dll")]
	private static extern bool InternetGetConnectedState(int Description, int ReservedValue);

	public static bool IsConnect()
	{
		return InternetGetConnectedState(0, 0);
	}

	public static bool IsConnectedInternet()
	{
		int num = 0;
		if (!InternetGetConnectedState(num, 0))
		{
			Console.WriteLine("当前没有联网，请您先联网后再进行操作！");
			if ((num & 0x14) == 0)
			{
				return false;
			}
			Console.WriteLine("本地系统处于脱机模式。");
			return false;
		}
		if ((num & 1) != 0)
		{
			Console.WriteLine("调制解调器上网。");
			return true;
		}
		if ((num & 2) != 0)
		{
			Console.WriteLine("网卡上网。");
			return true;
		}
		if ((num & 4) != 0)
		{
			Console.WriteLine("代理服务器上网。");
			return true;
		}
		if ((num & 0x40) != 0)
		{
			Console.WriteLine("虽然可以联网，但可能链接也可能不连接。");
			return true;
		}
		return false;
	}
}
