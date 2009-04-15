using System;
using System.Runtime.InteropServices;
using Microsoft.VisualStudio.OLE.Interop;

/// <summary> 
/// The IViewObject interface enables an object to display itself directly 
/// without passing a data object to the caller. In addition, this interface 
/// can create and manage a connection with an advise sink so the caller 
/// can be notified of changes in the view object. 
/// </summary> 
/// <remarks>This is implemented through com import, because this 
/// custom marshalling ensures correct work, the OLE.Interop implementation 
/// seems not to be valid. Please google for more information.</remarks> 
/// <seealso cref="Microsoft.VisualStudio.OLE.Interop.IViewObject"/> 
[ComVisible(true), ComImport()] 
[GuidAttribute("0000010d-0000-0000-C000-000000000046")] 
[InterfaceTypeAttribute(ComInterfaceType.InterfaceIsIUnknown)] 
public interface IViewObject 
{ 
	/// <summary> 
	/// Draws a representation of an object onto the specified device context. 
	/// </summary> 
	/// <param name="drawAspect">Aspect to be drawn</param> 
	/// <param name="index">Part of the object of interest in the draw operation</param> 
	/// <param name="aspectPointer">Pointer to DVASPECTINFO structure or NULL</param> 
	/// <param name="ptd">Pointer to target device in a structure</param> 
	/// <param name="hdcTargetDev">Information context for the target device</param> 
	/// <param name="hdcDraw">Device context on which to draw</param> 
	/// <param name="lprcBounds">Ref. The <see cref="RECTL"/> in which the object is drawn.</param> 
	/// <param name="lprcWBounds">Ref. Pointer to the window extent and window origin 
	/// when drawing a metafile.</param> 
	/// <param name="pfnContinue">Pointer to the callback function for canceling 
	/// or continuing the drawing</param> 
	/// <param name="doContinue">Value to pass to the callback function</param> 
	/// <returns>An error code.</returns> 
	[return: MarshalAs(UnmanagedType.I4)] 
		[PreserveSig] 
		int Draw( 
				[MarshalAs(UnmanagedType.U4)] DVASPECT drawAspect, 
				int index, 
				IntPtr aspectPointer, 
				[In] IntPtr ptd, 
				IntPtr hdcTargetDev, 
				IntPtr hdcDraw, 
				[MarshalAs(UnmanagedType.Struct)] ref RECTL lprcBounds, 
				[MarshalAs(UnmanagedType.Struct)] ref RECTL lprcWBounds, 
				IntPtr pfnContinue, 
				[MarshalAs(UnmanagedType.U4)] uint doContinue); 

	/// <summary> 
	/// Returns the logical palette that the object will use 
	/// for drawing in its IViewObject::Draw method with the corresponding parameters. 
	/// </summary> 
	/// <param name="drawAspect">How the object is to be represented</param> 
	/// <param name="index">Part of the object of interest in the draw operation</param> 
	/// <param name="aspectPointer">Always NULL</param> 
	/// <param name="ptd">Pointer to target device in a structure</param> 
	/// <param name="hicTargetDev">Information context for the target device</param> 
	/// <param name="colorSet">Out.Requested LOGPALETTE structure</param> 
	void GetColorSet( 
			[MarshalAs(UnmanagedType.U4)] uint drawAspect, 
			int index, 
			IntPtr aspectPointer, 
			[MarshalAs(UnmanagedType.Struct)] DVTARGETDEVICE ptd, 
			IntPtr hicTargetDev, 
			[Out, MarshalAs(UnmanagedType.Struct)] out LOGPALETTE colorSet); 

	/// <summary> 
	/// Freezes a certain aspect of the object's presentation so that 
	/// it does not change until the IViewObject::Unfreeze method is called. 
	/// The most common use of this method is for banded printing. 
	/// </summary> 
	/// <param name="drawAspect">How the object is to be represented</param> 
	/// <param name="index">Part of the object of interest in the draw operation</param> 
	/// <param name="aspectPointer">Always NULL</param> 
	/// <param name="pdwFreeze">Points to location containing an identifying key</param> 
	void Freeze( 
			[MarshalAs(UnmanagedType.U4)] uint drawAspect, 
			int index, 
			IntPtr aspectPointer, 
			out IntPtr pdwFreeze); 

	/// <summary> 
	/// Releases a previously frozen drawing. The most common use 
	/// of this method is for banded printing. 
	/// </summary> 
	/// <param name="freeze">Contains key that determines view object to unfreeze</param> 
	void Unfreeze([MarshalAs(UnmanagedType.U4)] int freeze); 

	/// <summary> 
	/// Sets up a connection between the view object and an advise 
	/// sink so that the advise sink can be notified about 
	/// changes in the object's view. 
	/// </summary> 
	/// <param name="aspects">View for which notification is being requested</param> 
	/// <param name="advf">Information about the advise sink</param> 
	/// <param name="adviseSink"><see cref="IAdviseSink"/> that is to receive change notifications</param> 
	void SetAdvise( 
			[MarshalAs(UnmanagedType.U4)] int aspects, 
			[MarshalAs(UnmanagedType.U4)] int advf, 
			[MarshalAs(UnmanagedType.Interface)] IAdviseSink adviseSink); 

	/// <summary> 
	/// Retrieves the existing advisory connection on the object if 
	/// there is one. This method simply returns the parameters used in 
	/// the most recent call to the IViewObject::SetAdvise method. 
	/// </summary> 
	/// <param name="paspects">Pointer to where dwAspect parameter from 
	/// previous SetAdvise call is returned</param> 
	/// <param name="advf">Pointer to where advf parameter from 
	/// previous SetAdvise call is returned</param> 
	/// <param name="adviseSink">Out. Receives the <see cref="IAdviseSink"/> interface</param> 
	void GetAdvise( 
			IntPtr paspects, 
			IntPtr advf, 
			[Out, MarshalAs(UnmanagedType.Interface)] out IAdviseSink adviseSink); 
}
