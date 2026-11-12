using System;
using GalaSoft.MvvmLight;

namespace distinguish.Model;

public class SendDataLog : ViewModelBase
{
	private DateTime seneDateTime;

	private string sendTimeStr;

	private bool autoSend;

	private int sendNumber;

	public DateTime SeneDateTime
	{
		get
		{
			return seneDateTime;
		}
		set
		{
			seneDateTime = value;
		}
	}

	public string SendTimeStr
	{
		get
		{
			return sendTimeStr;
		}
		set
		{
			sendTimeStr = value;
		}
	}

	public bool AutoSend
	{
		get
		{
			return autoSend;
		}
		set
		{
			autoSend = value;
		}
	}

	public int SendNumber
	{
		get
		{
			return sendNumber;
		}
		set
		{
			sendNumber = value;
		}
	}
}
