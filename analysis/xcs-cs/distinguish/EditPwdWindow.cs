using System;
using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.ViewModel;

namespace distinguish;

public class EditPwdWindow : Window, IComponentConnector
{
	internal TextBox ipAddrInput;

	internal TextBox userInput;

	internal TextBox pwdInput;

	internal Button loginBut;

	private bool _contentLoaded;

	public EditPwdWindow()
	{
		InitializeComponent();
		((FrameworkElement)this).DataContext = new EditPwdViewModel();
	}

	private void SaveEditPwdBut_Click(object sender, RoutedEventArgs e)
	{
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		if ((((FrameworkElement)this).DataContext as EditPwdViewModel).SaveEditPwd())
		{
			Application.Current.MainWindow.Close();
			((Window)new MainWindow()).Show();
			MessageBox.Show("修改密码成功");
			((Window)this).Close();
		}
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Uri uri = new Uri("/xcs;component/editpwdwindow.xaml", UriKind.Relative);
			Application.LoadComponent((object)this, uri);
		}
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	void IComponentConnector.Connect(int connectionId, object target)
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0026: Expected O, but got Unknown
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Expected O, but got Unknown
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_005a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0064: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			ipAddrInput = (TextBox)target;
			break;
		case 2:
			userInput = (TextBox)target;
			break;
		case 3:
			pwdInput = (TextBox)target;
			break;
		case 4:
			loginBut = (Button)target;
			((ButtonBase)loginBut).Click += new RoutedEventHandler(SaveEditPwdBut_Click);
			break;
		default:
			_contentLoaded = true;
			break;
		}
	}
}
