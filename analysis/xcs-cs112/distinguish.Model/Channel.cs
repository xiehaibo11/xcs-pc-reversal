using System.Collections.Generic;
using GalaSoft.MvvmLight;

namespace distinguish.Model;

public class Channel : ViewModelBase
{
	private int id;

	private int direction;

	private string directionName;

	private int online;

	private string onlineStr;

	private int run;

	private string runStr;

	private string ip;

	private int row;

	private int mj_count;

	private string serial_number;

	private List<ChannelMjResult> topMj;

	private List<ChannelMjResult> bottomMj;

	private List<MjNumber> mjNum;

	private int deleteOffset;

	public int Id
	{
		get
		{
			return id;
		}
		set
		{
			id = value;
		}
	}

	public int Direction
	{
		get
		{
			return direction;
		}
		set
		{
			direction = value;
		}
	}

	public string DirectionName
	{
		get
		{
			return directionName;
		}
		set
		{
			directionName = value;
		}
	}

	public int Online
	{
		get
		{
			return online;
		}
		set
		{
			online = value;
			((ObservableObject)this).RaisePropertyChanged("Online");
		}
	}

	public int Run
	{
		get
		{
			return run;
		}
		set
		{
			run = value;
			((ObservableObject)this).RaisePropertyChanged("Run");
		}
	}

	public string Ip
	{
		get
		{
			return ip;
		}
		set
		{
			ip = value;
			((ObservableObject)this).RaisePropertyChanged("Ip");
		}
	}

	public int Row
	{
		get
		{
			return row;
		}
		set
		{
			row = value;
		}
	}

	public List<ChannelMjResult> TopMj
	{
		get
		{
			return topMj;
		}
		set
		{
			topMj = value;
		}
	}

	public List<ChannelMjResult> BottomMj
	{
		get
		{
			return bottomMj;
		}
		set
		{
			bottomMj = value;
		}
	}

	public List<MjNumber> MjNum
	{
		get
		{
			return mjNum;
		}
		set
		{
			mjNum = value;
		}
	}

	public int Mj_count
	{
		get
		{
			return mj_count;
		}
		set
		{
			mj_count = value;
		}
	}

	public string RunStr
	{
		get
		{
			return runStr;
		}
		set
		{
			runStr = value;
			((ObservableObject)this).RaisePropertyChanged("RunStr");
		}
	}

	public string OnlineStr
	{
		get
		{
			if (online == 1)
			{
				return "在线";
			}
			return "未在线";
		}
		set
		{
			onlineStr = value;
			((ObservableObject)this).RaisePropertyChanged("OnlineStr");
		}
	}

	public string Serial_number
	{
		get
		{
			return serial_number;
		}
		set
		{
			serial_number = value;
		}
	}

	public int DeleteOffset
	{
		get
		{
			return deleteOffset;
		}
		set
		{
			deleteOffset = value;
		}
	}

	public Channel(string index)
	{
		topMj = new List<ChannelMjResult>();
		bottomMj = new List<ChannelMjResult>();
		mjNum = new List<MjNumber>();
		for (int i = 0; i < 20; i++)
		{
			topMj.Add(new ChannelMjResult(index + "top:" + i));
			bottomMj.Add(new ChannelMjResult(index + "bottom:" + i));
			mjNum.Add(new MjNumber((i + 1).ToString()));
		}
		ip = "-.-.-.-";
	}

	public void setMjNumber(string index, int mjNumber)
	{
		topMj = new List<ChannelMjResult>();
		bottomMj = new List<ChannelMjResult>();
		mjNum = new List<MjNumber>();
		for (int i = 0; i < mjNumber; i++)
		{
			topMj.Add(new ChannelMjResult(index + "top:" + i));
			bottomMj.Add(new ChannelMjResult(index + "bottom:" + i));
			mjNum.Add(new MjNumber((i + 1).ToString()));
		}
	}
}
