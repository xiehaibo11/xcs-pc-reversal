namespace distinguish.Model;

public class WifiConfig
{
	private string ssid;

	private string pwd;

	public string Ssid
	{
		get
		{
			return ssid;
		}
		set
		{
			ssid = value;
		}
	}

	public string Pwd
	{
		get
		{
			return pwd;
		}
		set
		{
			pwd = value;
		}
	}
}
