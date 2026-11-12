using System.Collections.Generic;

namespace distinguish.Model;

internal class AppVersion
{
	private int type;

	private string version;

	private string update_url;

	private Dictionary<string, string> ota_list;

	public int Type
	{
		get
		{
			return type;
		}
		set
		{
			type = value;
		}
	}

	public string Version
	{
		get
		{
			return version;
		}
		set
		{
			version = value;
		}
	}

	public string Update_url
	{
		get
		{
			return update_url;
		}
		set
		{
			update_url = value;
		}
	}

	public Dictionary<string, string> Ota_list
	{
		get
		{
			return ota_list;
		}
		set
		{
			ota_list = value;
		}
	}
}
