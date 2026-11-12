using System.Windows;
using GalaSoft.MvvmLight;

namespace distinguish.Model;

public class ChannelMjResult : ViewModelBase
{
	private string index;

	private string result;

	private double resultJd;

	private string resultImg;

	private string reslut180;

	private double result180Jd;

	private string result180Img;

	private int mahjongNumber;

	private string uploadImg;

	private bool validPageSelect;

	private Visibility imgVis;

	public string Result
	{
		get
		{
			return result;
		}
		set
		{
			result = value;
		}
	}

	public double ResultJd
	{
		get
		{
			return resultJd;
		}
		set
		{
			resultJd = value;
		}
	}

	public string ResultImg
	{
		get
		{
			return resultImg;
		}
		set
		{
			resultImg = value;
			((ObservableObject)this).RaisePropertyChanged("ResultImg");
		}
	}

	public string Reslut180
	{
		get
		{
			return reslut180;
		}
		set
		{
			reslut180 = value;
		}
	}

	public double Result180Jd
	{
		get
		{
			return result180Jd;
		}
		set
		{
			result180Jd = value;
		}
	}

	public string Result180Img
	{
		get
		{
			return result180Img;
		}
		set
		{
			result180Img = value;
		}
	}

	public int MahjongNumber
	{
		get
		{
			return mahjongNumber;
		}
		set
		{
			mahjongNumber = value;
		}
	}

	public string UploadImg
	{
		get
		{
			return uploadImg;
		}
		set
		{
			uploadImg = value;
			((ObservableObject)this).RaisePropertyChanged("UploadImg");
		}
	}

	public string Index
	{
		get
		{
			return index;
		}
		set
		{
			index = value;
		}
	}

	public bool ValidPageSelect
	{
		get
		{
			return validPageSelect;
		}
		set
		{
			validPageSelect = value;
			((ObservableObject)this).RaisePropertyChanged("ValidPageSelect");
		}
	}

	public Visibility ImgVis
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return imgVis;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			imgVis = value;
			((ObservableObject)this).RaisePropertyChanged("ImgVis");
		}
	}

	public ChannelMjResult(string index)
	{
		Index = index;
		UploadImg = "/res/bottom_bg.jpg";
		result180Img = "/res/top_bg.jpg";
		resultImg = "/res/top_bg.jpg";
		ImgVis = (Visibility)0;
	}
}
