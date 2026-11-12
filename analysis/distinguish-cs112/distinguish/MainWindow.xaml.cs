using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.Common;
using distinguish.ViewModel;

namespace distinguish;

public partial class MainWindow : Window, IComponentConnector
{
	internal TextBox ipAddrInput;

	internal TextBox userInput;

	internal TextBox pwdInput;

	internal Button loginBut;

	internal Button resetBut;

	public MainWindow()
	{
		Global.RemoveCacheImg();
		if (!Global.verify)
		{
			((Window)new ErrorWindow()).Show();
			((Window)this).Close();
		}
		InitializeComponent();
		((FrameworkElement)this).DataContext = new LoginViewModel();
	}

	private void CheckBox_Checked(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as LoginViewModel).SavePwdCheckBox = true;
	}

	private void CheckBox_Unchecked(object sender, RoutedEventArgs e)
	{
		(((FrameworkElement)this).DataContext as LoginViewModel).SavePwdCheckBox = false;
	}

	private void Button_Click(object sender, RoutedEventArgs e)
	{
		Application.Current.Shutdown();
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Expected Obj, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected Obj, but got Unknown
		//IL_0050: Unknown result type (might be due to invalid IL or missing references)
		//IL_005a: Expected Obj, but got Unknown
		//IL_005d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0067: Expected Obj, but got Unknown
		//IL_0069: Unknown result type (might be due to invalid IL or missing references)
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_007f: Expected Obj, but got Unknown
		//IL_0080: Unknown result type (might be due to invalid IL or missing references)
		//IL_008c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0096: Expected Obj, but got Unknown
		//IL_0099: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a3: Expected Obj, but got Unknown
		//IL_00a6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b0: Expected Obj, but got Unknown
		switch (connectionId)
		{
		case 1:
			((ButtonBase)(Button)target).Click += Button_Click;
			break;
		case 2:
			ipAddrInput = (TextBox)target;
			break;
		case 3:
			userInput = (TextBox)target;
			break;
		case 4:
			pwdInput = (TextBox)target;
			break;
		case 5:
			((ToggleButton)(CheckBox)target).Checked += CheckBox_Checked;
			((ToggleButton)(CheckBox)target).Unchecked += CheckBox_Unchecked;
			break;
		case 6:
			loginBut = (Button)target;
			break;
		case 7:
			resetBut = (Button)target;
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
