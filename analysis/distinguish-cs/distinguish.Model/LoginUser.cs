namespace distinguish.Model;

public class LoginUser
{
	private int id;

	private string username;

	private string token;

	private int login_type;

	private long exp;

	private long nbf;

	private int jihuo_status;

	private int data_send;

	private int timeout;

	private int delay;

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

	public string Username
	{
		get
		{
			return username;
		}
		set
		{
			username = value;
		}
	}

	public string Token
	{
		get
		{
			return token;
		}
		set
		{
			token = value;
		}
	}

	public int Login_type
	{
		get
		{
			return login_type;
		}
		set
		{
			login_type = value;
		}
	}

	public long Exp
	{
		get
		{
			return exp;
		}
		set
		{
			exp = value;
		}
	}

	public long Nbf
	{
		get
		{
			return nbf;
		}
		set
		{
			nbf = value;
		}
	}

	public int JihuoStatus
	{
		get
		{
			return jihuo_status;
		}
		set
		{
			jihuo_status = value;
		}
	}

	public int DataSend
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
}
