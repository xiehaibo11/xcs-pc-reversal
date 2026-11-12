using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Reflection;
using System.Windows;

namespace distinguish;

public class App : Application
{
	private bool _contentLoaded;

	protected override void OnStartup(StartupEventArgs e)
	{
		((Application)this).OnStartup(e);
		AppDomain.CurrentDomain.AssemblyResolve += OnResolveAssembly;
	}

	private static Assembly OnResolveAssembly(object sender, ResolveEventArgs args)
	{
		Assembly executingAssembly = Assembly.GetExecutingAssembly();
		AssemblyName name = executingAssembly.GetName();
		string text = name.Name + ".resources";
		AssemblyName assemblyName = new AssemblyName(args.Name);
		string text2 = "";
		if (text == assemblyName.Name)
		{
			text2 = name.Name + ".g.resources";
		}
		else
		{
			text2 = assemblyName.Name + ".dll";
			if (assemblyName.CultureInfo != null && !assemblyName.CultureInfo.Equals(CultureInfo.InvariantCulture))
			{
				text2 = $"{assemblyName.CultureInfo}\\{text2}";
			}
		}
		using Stream stream = executingAssembly.GetManifestResourceStream(text2);
		if (stream == null)
		{
			return null;
		}
		byte[] array = new byte[stream.Length];
		stream.Read(array, 0, array.Length);
		return Assembly.Load(array);
	}

	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			((Application)this).StartupUri = new Uri("LoginWindow.xaml", UriKind.Relative);
			Uri uri = new Uri("/xcs;component/app.xaml", UriKind.Relative);
			Application.LoadComponent((object)this, uri);
		}
	}

	[STAThread]
	[DebuggerNonUserCode]
	[GeneratedCode("PresentationBuildTasks", "4.0.0.0")]
	public static void Main()
	{
		App app = new App();
		app.InitializeComponent();
		((Application)app).Run();
	}
}
