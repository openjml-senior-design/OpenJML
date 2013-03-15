/*
 * This file is part of the OpenJML plugin project.
 * Copyright 2004-2013 David R. Cok
 */
package org.jmlspecs.openjml.eclipse;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jmlspecs.openjml.eclipse.widgets.ButtonFieldEditor;
import org.jmlspecs.openjml.eclipse.widgets.LabelFieldEditor;

// FIXME - review for other options

/**
 * This class creates a Preferences page in Eclipse
 * This page hold data needed for CodeSonar and Eclipse interaction
 * <P>
 * The preferences page manages various JML and OpenJML and plug-in specific
 * options. As usual these are stored in the preferences store, but OpenJML
 * uses the System properties to hold values of options, so we need also to 
 * store new changed values in the System properties. Also, we have the
 * unusual functionality to initialize the Eclipse preferences from properties.
 * <P>
 * The key used for preferences is the same as the key used in System properties.
 * <P>
 * Notes: Field editors are a convenient way to create a preferences page,
 * but not quite convenient enough on a couple of counts. 
 * <UL>
 * <LI> We need to observe when fields are changed. The normal way to do this
 * would be to register a listener, but only one listener is allowed, and 
 * the implementation of FieldEditorPreferencePage overwrites (during initialize())
 * any listener added when a field editor is created.
 * <LI> So we have to put the propertyChange call on the derived SettingsPage
 * itself, which means that it is the same listener for all fields.
 * <LI> There is insufficient access to FieldEditorPreferencePage functionality
 * such as the list of all fields or being able to force loads and stores.
 * </UL>
 */

public class PreferencesPage extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

	public PreferencesPage() {
		super(FLAT);
		// No descriptive text needed. setDescription("Options for OpenJML");
	}
	
    public void init(IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }
    
    /** A mapping of option keys to field names */
    protected Map<String,FieldEditor> fieldMap = new HashMap<String,FieldEditor>();

    /** Overridden to add the field to the fieldMap */
    @Override
    public void addField(FieldEditor field) {
    	super.addField(field);
    	fieldMap.put(field.getPreferenceName(), field);
    }
    

    /** Cached copy of the verbosity editor so we can handle its change
     * events specially.
     */
    protected FieldEditor verbosity;

    /** Overriding performOk() callback in order to maintain copies
     * of the option values appropriately. This is called when
     * 'Apply' or 'OK' is clicked.
     */
	@Override
	public boolean performOk() {
		super.performOk();
		String value = getPreferenceStore().getString(Options.verbosityKey);
        Utils.verboseness = Integer.parseInt((String)value);
        return true;
	}
	
	/** The method that constructs all the editors and arranges them on the
	 * settings page.
	 */
    @Override
    protected void createFieldEditors() {
    	
    	// JML
    	
    	MouseListener listener = new MouseAdapter() {
    		@Override
			public void mouseUp(MouseEvent e) {
				Properties properties = Utils.getProperties();
				for (Map.Entry<Object,Object> entry : properties.entrySet()) {
					Object keyobj = entry.getKey();
					if (!(keyobj instanceof String)) continue;
					String key = (String)keyobj;
					if (!(entry.getValue() instanceof String)) continue;
					String value = (String)entry.getValue();
					if (key.contains(Utils.OPENJML)) {
						if (Utils.verboseness >= Utils.DEBUG) {
							Log.log("Reading property: " + key + " = " + value); //$NON-NLS-1$ //$NON-NLS-2$
						}
						FieldEditor field = fieldMap.get(key);
						if (field != null) {
							if (field instanceof BooleanFieldEditor) {
								getPreferenceStore().setValue(key,!value.isEmpty());
							} else if (field instanceof StringFieldEditor) {
								getPreferenceStore().setValue(key,value);
							} else if (field instanceof ComboFieldEditor) {
								getPreferenceStore().setValue(key,value); // FIXME - how do we know it is a valid value
								if (field == verbosity) Utils.verboseness = Integer.parseInt(value);
							} else {
								Log.errorKey("openjml.ui.unknown.field.editor",null,field.getClass(),key,value);  //$NON-NLS-1$
							}
						} else {
							// Assume anything else has a String value
							getPreferenceStore().setValue(key,value);
						}
					} else {
						// There are lots of these - mostly Java or Eclipse related
						//Log.log("Ignoring property " + key + "=" + value);
					}
				}
				initialize();
			}
		};
		
		addField(new ButtonFieldEditor(Options.updateKey,"", //$NON-NLS-1$
				Messages.OpenJMLUI_PreferencesPage_UpdateFromPropertiesFiles,
				listener,
				getFieldEditorParent())
		);

		addField(new LabelFieldEditor("zzzzz.JML","",SWT.NONE, //$NON-NLS-1$ //$NON-NLS-2$
				getFieldEditorParent()));
		addField(new LabelFieldEditor("zzzzz.JML",Messages.OpenJMLUI_PreferencesPage_JmlOptions,SWT.SEPARATOR|SWT.HORIZONTAL, //$NON-NLS-1$
				getFieldEditorParent()));

        addField(new BooleanFieldEditor(Options.nonnullByDefaultKey, Messages.OpenJMLUI_PreferencesPage_NonNullByDefault,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(Options.checkPurityKey, Messages.OpenJMLUI_PreferencesPage_SkipPurityCheck,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(Options.showNotImplementedKey, Messages.OpenJMLUI_PreferencesPage_WarnAboutNonImplementedConstructs,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(Options.showNotExecutableKey, Messages.OpenJMLUI_PreferencesPage_WarnAboutNonExecutableConstructs,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(Options.checkSpecsPathKey, Messages.OpenJMLUI_PreferencesPage_CheckSpecificationPath,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(Options.noInternalSpecsKey, Messages.OpenJMLUI_PreferencesPage_UseExternalSystemSpecs,
                getFieldEditorParent()));
        addField(new StringFieldEditor(Options.optionalKeysKey, Messages.OpenJMLUI_PreferencesPage_OptionalAnnotationKeys,
                getFieldEditorParent()));
        
        
        // RAC
        
		addField(new LabelFieldEditor("zzzzz.RAC","",SWT.NONE, //$NON-NLS-1$ //$NON-NLS-2$
				getFieldEditorParent()));
		addField(new LabelFieldEditor("zzzzz.RAC",Messages.OpenJMLUI_PreferencesPage_OptionsRelatingToRAC,SWT.SEPARATOR|SWT.HORIZONTAL, //$NON-NLS-1$
				getFieldEditorParent()));

        addField(new BooleanFieldEditor(Options.enableRacKey, Messages.OpenJMLUI_PreferencesPage_EnableAutoRuntimeAssertionChecking,
                getFieldEditorParent()));
        addField(new StringFieldEditor(Options.racbinKey, Messages.OpenJMLUI_PreferencesPage_DirectoryForRACOutput,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(Options.noInternalRuntimeKey, Messages.OpenJMLUI_PreferencesPage_UseExternalRuntimeLibrary,
                getFieldEditorParent()));

        // Debug and verbosity

		addField(new LabelFieldEditor("zzzzz.VERBOSE","",SWT.NONE, //$NON-NLS-1$ //$NON-NLS-2$
				getFieldEditorParent()));
		addField(new LabelFieldEditor("zzzzz.VERBOSE",Messages.OpenJMLUI_PreferencesPage_VerbosenessAndDebugging,SWT.SEPARATOR|SWT.HORIZONTAL, //$NON-NLS-1$
				getFieldEditorParent()));

        addField(new BooleanFieldEditor(Options.javaverboseKey, Messages.OpenJMLUI_PreferencesPage_JavaVerbose,
                getFieldEditorParent()));
        
        addField(verbosity=new ComboFieldEditor(Options.verbosityKey, Messages.OpenJMLUI_PreferencesPage_VerbosityLevel,
        		new String[][]{ 
        			{Messages.OpenJMLUI_PreferencesPage_quiet, Integer.toString(Utils.QUIET) }, 
        			{Messages.OpenJMLUI_PreferencesPage_normal, Integer.toString(Utils.NORMAL)}, 
        		    {Messages.OpenJMLUI_PreferencesPage_progress, Integer.toString(Utils.PROGRESS)}, 
        		    {Messages.OpenJMLUI_PreferencesPage_verbose, Integer.toString(Utils.VERBOSE)}, 
        		    {Messages.OpenJMLUI_PreferencesPage_debug, Integer.toString(Utils.DEBUG)}},
                getFieldEditorParent()));
    }
    

}