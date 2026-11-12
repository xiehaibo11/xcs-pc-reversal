namespace distinguish.Model;

internal class MjConfig
{
	private int number;

	private string name;

	private int id;

	private string url;

	public int Number
	{
		get
		{
			return number;
		}
		set
		{
			number = value;
		}
	}

	public string Name
	{
		get
		{
			return name;
		}
		set
		{
			name = value;
		}
	}

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

	public string Url
	{
		get
		{
			return url;
		}
		set
		{
			url = value;
		}
	}

	public MjConfig(int id, int number, string name, string url)
	{
		this.number = number;
		this.name = name;
		this.id = id;
		this.url = url;
	}
}
