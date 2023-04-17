package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vividbobo.easy.databinding.FragmentTransferBinding;

public class TransferFragment extends Fragment {
    private static final String TAG = "TransferFragment";

    public static TransferFragment newInstance() {
        Bundle args = new Bundle();

        TransferFragment fragment = new TransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentTransferBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransferBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}
