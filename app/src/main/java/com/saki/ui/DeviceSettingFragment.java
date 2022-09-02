package com.saki.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ayzf.anwind.R;

public class DeviceSettingFragment extends PreferenceFragment
{
@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addPreferencesFromResource(R.xml.devicesetting);
        final View v =super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }
}

