using System;
using System.Globalization;
using System.IO;
using System.Reflection;
using System.Windows;

namespace distinguish;

public partial class App : Application
{
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
}
