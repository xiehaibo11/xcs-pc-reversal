using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using WebSocketSharp;

namespace distinguish.Common;

internal class NetManager
{
	private static WebSocket WebSockets;

	public static string LoginData;

	public static Dictionary<string, long> qosList;

	public static Action<string> OnMessage;

	public static Action<bool> OnNetStateChange;

	private static bool IsClose;

	private static bool IsReconnectioning;

	public static bool IsConnect
	{
		get
		{
			if (WebSockets == null)
			{
				return false;
			}
			return WebSockets.IsAlive;
		}
	}

	public static string IPAddress { get; set; }

	public static bool IsShowPingLog { get; set; }

	public static void Init()
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_0021: Expected O, but got Unknown
		if (!string.IsNullOrEmpty(IPAddress))
		{
			WebSockets = new WebSocket(IPAddress, Array.Empty<string>());
			WebSockets.OnOpen += WebSocket_OnOpen;
			WebSockets.OnMessage += WebSocket_OnMessage;
			WebSockets.OnClose += WebSocket_OnClose;
			WebSockets.OnError += WebSocket_OnError;
			Console.WriteLine("netManager初始化完成");
		}
	}

	private static void WebSocket_OnMessage(object sender, MessageEventArgs e)
	{
		Console.WriteLine("[NetManager][OnMessage]:" + DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() + ":" + e.Data);
		if (e.IsText)
		{
			if (OnMessage != null)
			{
				OnMessage(e.Data);
			}
		}
		else if (e.IsBinary)
		{
			Console.WriteLine(e.RawData);
		}
	}

	private static void WebSocket_OnError(object sender, ErrorEventArgs e)
	{
		Console.WriteLine($"[NetManager][OnError]{e.Message}");
	}

	private static void WebSocket_OnClose(object sender, CloseEventArgs e)
	{
		Console.WriteLine("[NetManager][OnClose]" + e.Reason);
		if (OnNetStateChange != null)
		{
			OnNetStateChange(obj: false);
		}
		if (!string.IsNullOrEmpty(e.Reason) && !IsClose)
		{
			DisconnectionReconnects();
		}
	}

	private static void WebSocket_OnOpen(object sender, EventArgs e)
	{
		Console.WriteLine("[NetManager][OnOpen] 时间：{0}", DateTime.Now);
		if (OnNetStateChange != null)
		{
			OnNetStateChange(obj: true);
		}
	}

	public static bool Connect(string loginData)
	{
		LoginData = loginData;
		if (WebSockets == null)
		{
			return false;
		}
		if (WebSockets.IsAlive)
		{
			return false;
		}
		WebSockets.Connect();
		if (WebSockets.IsAlive)
		{
			IsClose = false;
			Send(loginData);
			return true;
		}
		return false;
	}

	public static bool Disconnect()
	{
		if (WebSockets == null)
		{
			return false;
		}
		WebSockets.Close();
		IsClose = true;
		return !WebSockets.IsAlive;
	}

	public static void Send(string message)
	{
		if (string.IsNullOrEmpty(message))
		{
			Console.WriteLine("[NetManager]发送的消息不能为空");
			return;
		}
		if (WebSockets == null)
		{
			Console.WriteLine("[NetManager]请先打开服务器");
			Init();
			return;
		}
		Console.WriteLine("[netManager] send Async start:" + DateTimeOffset.UtcNow.ToUnixTimeMilliseconds());
		try
		{
			Console.WriteLine("[netManager] start:" + message);
			WebSockets.Send(message);
			Console.WriteLine("[netManager] success:" + message);
		}
		catch (Exception ex)
		{
			Console.WriteLine(ex.ToString());
		}
	}

	public static async void DisconnectionReconnects()
	{
		if (IsReconnectioning)
		{
			return;
		}
		IsReconnectioning = true;
		while (true)
		{
			if (WebSockets.IsAlive)
			{
				Console.WriteLine("[NetManager]断线重连成功");
				Send(LoginData);
				IsReconnectioning = false;
				return;
			}
			if (IsClose)
			{
				break;
			}
			Console.WriteLine("[NetManager]断线重连中......");
			WebSockets.Connect();
			await Task.Delay(TimeSpan.FromSeconds(1.0));
		}
		Console.WriteLine("[NetManager]关闭连接不必断线重连");
	}

	static NetManager()
	{
		WebSockets = null;
		qosList = new Dictionary<string, long>();
		IsShowPingLog = false;
		OnMessage = null;
		OnNetStateChange = null;
		IsClose = false;
		IsReconnectioning = false;
	}

	private NetManager()
	{
	}
}
