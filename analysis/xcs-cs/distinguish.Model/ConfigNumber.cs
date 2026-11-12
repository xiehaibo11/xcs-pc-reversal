using GalaSoft.MvvmLight;

namespace distinguish.Model;

public class ConfigNumber : ViewModelBase
{
	private int east;

	private int south;

	private int west;

	private int north;

	public int East
	{
		get
		{
			return east;
		}
		set
		{
			east = value;
			((ObservableObject)this).RaisePropertyChanged("East");
		}
	}

	public int South
	{
		get
		{
			return south;
		}
		set
		{
			south = value;
			((ObservableObject)this).RaisePropertyChanged("South");
		}
	}

	public int West
	{
		get
		{
			return west;
		}
		set
		{
			west = value;
			((ObservableObject)this).RaisePropertyChanged("West");
		}
	}

	public int North
	{
		get
		{
			return north;
		}
		set
		{
			north = value;
			((ObservableObject)this).RaisePropertyChanged("North");
		}
	}
}
