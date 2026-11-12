namespace distinguish.Model;

public class DataConfig
{
	private int data_send;

	private int timeout;

	private int delay;

	private int count_type;

	public int Data_send
	{
		get
		{
			return data_send;
		}
		set
		{
			data_send = value;
		}
	}

	public int Timeout
	{
		get
		{
			return timeout;
		}
		set
		{
			timeout = value;
		}
	}

	public int Delay
	{
		get
		{
			return delay;
		}
		set
		{
			delay = value;
		}
	}

	public int Count_type
	{
		get
		{
			return count_type;
		}
		set
		{
			count_type = value;
		}
	}
}
