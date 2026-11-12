using System;
using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Threading;
using distinguish.Common;

namespace distinguish;

public partial class VerifyPwdWindow : Window, IComponentConnector
{
	public bool toLogin = true;

	internal TextBox pwdInput;

	public VerifyPwdWindow()
	{
		InitializeComponent();
		((DispatcherObject)this).Dispatcher.BeginInvoke((DispatcherPriority)4, (Delegate)(Action)(() =>
		{
			Keyboard.Focus((IInputElement)(object)pwdInput);
		}));
	}

	private void Button_Click(object sender, RoutedEventArgs e)
	{
		_save();
	}

	private void pwdInput_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 6)
		{
			_save();
		}
	}

	private void _save()
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		if (pwdInput.Text == "")
		{
			MessageBox.Show("请输入专属密钥");
			((UIElement)pwdInput).Focus();
			return;
		}
		string md = Global.GetMd5(pwdInput.Text);
		if (md == Global.verifyDefaultPwd)
		{
			MessageBox.Show("专属密钥不能与初始化密钥一致！");
			return;
		}
		AppConfig.setConfig(Constant.INI_VERIFY_ROOT, Constant.INI_VERIFY_PWD, md);
		if (toLogin)
		{
			((Window)new MainWindow()).Show();
		}
		((Window)this).Close();
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0016: Expected Obj, but got Unknown
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		//IL_002d: Expected Obj, but got Unknown
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_003b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected Obj, but got Unknown
		switch (connectionId)
		{
		case 1:
			pwdInput = (TextBox)target;
			((UIElement)pwdInput).KeyUp += pwdInput_KeyUp;
			break;
		case 2:
			((ButtonBase)(Button)target).Click += Button_Click;
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
