using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Windows;
using GalaSoft.MvvmLight;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using distinguish.Common;

namespace distinguish.ViewModel;

public class EditPwdViewModel : ViewModelBase
{
	private string oldPwd;

	private string newPwd;

	private string reNewPwd;

	private bool loginButIsDisable;

	public string loginButContent;

	public string LoginButContent
	{
		get
		{
			return loginButContent;
		}
		set
		{
			loginButContent = value;
			((ObservableObject)this).RaisePropertyChanged("LoginButContent");
		}
	}

	public string OldPwd
	{
		get
		{
			return oldPwd;
		}
		set
		{
			oldPwd = value;
			((ObservableObject)this).RaisePropertyChanged("OldPwd");
		}
	}

	public string NewPwd
	{
		get
		{
			return newPwd;
		}
		set
		{
			newPwd = value;
			((ObservableObject)this).RaisePropertyChanged("NewPwd");
		}
	}

	public string ReNewPwd
	{
		get
		{
			return reNewPwd;
		}
		set
		{
			reNewPwd = value;
			((ObservableObject)this).RaisePropertyChanged("ReNewPwd");
		}
	}

	public bool LoginButIsDisable
	{
		get
		{
			return loginButIsDisable;
		}
		set
		{
			loginButIsDisable = value;
			((ObservableObject)this).RaisePropertyChanged("LoginButIsDisable");
		}
	}

	public EditPwdViewModel()
	{
		oldPwd = "";
		newPwd = "";
		reNewPwd = "";
		loginButIsDisable = true;
		loginButContent = "立即修改密码";
	}

	public bool SaveEditPwd()
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d7: Expected Obj, but got Unknown
		//IL_0064: Unknown result type (might be due to invalid IL or missing references)
		//IL_0121: Unknown result type (might be due to invalid IL or missing references)
		if (oldPwd.Length == 0)
		{
			MessageBox.Show("请输入旧密码", "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
			return false;
		}
		if (newPwd.Length == 0)
		{
			MessageBox.Show("请输入新密码", "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
			return false;
		}
		if (newPwd != reNewPwd)
		{
			MessageBox.Show("新密码与重复新密码不一致", "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
			return false;
		}
		LoginButIsDisable = false;
		LoginButContent = "保存中...";
		new HttpClient();
		string jsonData = JsonConvert.SerializeObject((object)new Dictionary<string, object>
		{
			["old_password"] = oldPwd,
			["password"] = newPwd
		});
		string text = HttpReq.post("account/edit/pwd", jsonData);
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		JsonSerializer val = new JsonSerializer();
		((Collection<JsonConverter>)(object)val.Converters).Add((JsonConverter)new JavaScriptDateTimeConverter());
		val.NullValueHandling = (NullValueHandling)1;
		dictionary = JsonConvert.DeserializeObject<Dictionary<string, object>>(text);
		if ((bool)dictionary["success"])
		{
			return true;
		}
		LoginButIsDisable = true;
		LoginButContent = "立即修改密码";
		MessageBox.Show(dictionary["msg"].ToString(), "错误提示", (MessageBoxButton)0, (MessageBoxImage)16);
		return false;
	}
}
