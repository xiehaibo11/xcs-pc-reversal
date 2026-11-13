using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Markup;
using distinguish.ViewModel;

namespace distinguish;

public partial class EditPwdWindow : Window, IComponentConnector
{
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
}
