using GalaSoft.MvvmLight;

namespace distinguish.Model;

public class MjNumber : ViewModelBase
{
	private string val;

	public string Val
	{
		get
		{
			return val;
		}
		set
		{
			val = value;
		}
	}

	public MjNumber(string val)
	{
		Val = val;
	}
}
