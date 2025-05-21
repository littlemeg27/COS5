package com.example.kayakquest.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.example.kayakquest.R;

public class SignInFragment extends Fragment
{
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Set up sign-in button
        Button signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> signIn());

        return view;
    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(account ->
                    {
                        String idToken = account.getIdToken();
                        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>()
                                {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(getContext(), "Signed in as " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Sign-In Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}