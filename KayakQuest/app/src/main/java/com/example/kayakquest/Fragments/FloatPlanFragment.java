package com.example.kayakquest.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.kayakquest.Operations.FloatPlan;
import com.example.kayakquest.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FloatPlanFragment extends Fragment
{
    private TextInputEditText name, phoneNumberInput, ageInput, address, city, emergencyName,
            emergencyPhoneNumberInput, kayakMake, kayakModel, kayakLength, safetyEquipmentNotes,
            vehicleModel, departure, arrival, tripNotes;
    private android.widget.Spinner genderDropdown, stateDropdown, kayakColorDropdown, carMakeDropdown;
    private DatePicker datePicker;
    private TimePicker startTimePicker, endTimePicker;
    private MaterialButton btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_float_plan, container, false);

        name = view.findViewById(R.id.name);
        genderDropdown = view.findViewById(R.id.gender_dropdown);
        phoneNumberInput = view.findViewById(R.id.phone_number_input);
        ageInput = view.findViewById(R.id.age_input);
        address = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        stateDropdown = view.findViewById(R.id.state_dropdown);
        emergencyName = view.findViewById(R.id.emergency_name);
        emergencyPhoneNumberInput = view.findViewById(R.id.emergency_phone_number_input);
        kayakMake = view.findViewById(R.id.kayak_make);
        kayakModel = view.findViewById(R.id.kayak_model);
        kayakLength = view.findViewById(R.id.kayak_length);
        kayakColorDropdown = view.findViewById(R.id.kayak_dropdown);
        safetyEquipmentNotes = view.findViewById(R.id.safety_equipment_notes);
        carMakeDropdown = view.findViewById(R.id.car_make_dropdown);
        vehicleModel = view.findViewById(R.id.vehicle_model);
        datePicker = view.findViewById(R.id.date_picker);
        departure = view.findViewById(R.id.departure);
        startTimePicker = view.findViewById(R.id.start_time_picker);
        arrival = view.findViewById(R.id.arrival);
        endTimePicker = view.findViewById(R.id.end_time_picker);
        tripNotes = view.findViewById(R.id.trip_notes);
        btnSubmit = view.findViewById(R.id.btn_submit);

        // Setup spinners
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.dropdown_gender, android.R.layout.simple_spinner_dropdown_item);
        genderDropdown.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.dropdown_states, android.R.layout.simple_spinner_dropdown_item);
        stateDropdown.setAdapter(stateAdapter);

        ArrayAdapter<CharSequence> kayakColorAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.dropdown_kayak, android.R.layout.simple_spinner_dropdown_item);
        kayakColorDropdown.setAdapter(kayakColorAdapter);

        ArrayAdapter<CharSequence> vehicleMakeAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.dropdown_car_make, android.R.layout.simple_spinner_dropdown_item);
        carMakeDropdown.setAdapter(vehicleMakeAdapter);

        btnSubmit.setOnClickListener(v ->
        {
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
            {
                Toast.makeText(requireContext(), "Please sign in first", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_floatPlanFragment_to_signInFragment);
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            String departureDate = dateFormat.format(calendar.getTime());

            FloatPlan floatPlan = new FloatPlan(
                    name.getText().toString(),
                    genderDropdown.getSelectedItem().toString(),
                    phoneNumberInput.getText().toString(),
                    parseIntOrZero(ageInput.getText().toString()),
                    address.getText().toString(),
                    city.getText().toString(),
                    stateDropdown.getSelectedItem().toString(),
                    emergencyName.getText().toString(),
                    emergencyPhoneNumberInput.getText().toString(),
                    kayakMake.getText().toString(),
                    kayakModel.getText().toString(),
                    kayakLength.getText().toString(),
                    kayakColorDropdown.getSelectedItem().toString(),
                    safetyEquipmentNotes.getText().toString(),
                    carMakeDropdown.getSelectedItem().toString(),
                    vehicleModel.getText().toString(),
                    departureDate,
                    String.format(Locale.US, "%02d:%02d", startTimePicker.getHour(), startTimePicker.getMinute()),
                    departure.getText().toString(),
                    arrival.getText().toString(),
                    String.format(Locale.US, "%02d:%02d", endTimePicker.getHour(), endTimePicker.getMinute()),
                    tripNotes.getText().toString(),
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    null
            );

            File pdfFile = new File(requireContext().getFilesDir(), "float_plan_" + System.currentTimeMillis() + ".pdf");
            createFloatPlanPdf(floatPlan, pdfFile.getAbsolutePath());
            uploadFloatPlanPdf(floatPlan, pdfFile, downloadUrl ->
            {
                if (downloadUrl != null)
                {
                    floatPlan.setPdfUrl(downloadUrl);
                    saveFloatPlanMetadata(floatPlan, floatPlanId ->
                    {
                        if (floatPlanId != null)
                        {
                            shareFloatPlan(floatPlanId);
                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Failed to save metadata", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(requireContext(), "Failed to upload PDF", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private int parseIntOrZero(String value)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    private void createFloatPlanPdf(FloatPlan floatPlan, String outputPath)
    {
        try
        {
            File pdfFile = new File(outputPath);
            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("Kayak Float Plan").setBold());
            document.add(new Paragraph("Kayaker Information"));
            document.add(new Paragraph("Name: " + floatPlan.getKayakerName()));
            document.add(new Paragraph("Gender: " + floatPlan.getGender()));
            document.add(new Paragraph("Phone: " + floatPlan.getPhoneNumber()));
            document.add(new Paragraph("Age: " + floatPlan.getAge()));
            document.add(new Paragraph("Address: " + floatPlan.getAddress() + ", " + floatPlan.getCity() + ", " + floatPlan.getState()));
            document.add(new Paragraph("Emergency Contact: " + floatPlan.getEmergencyContact() + ", " + floatPlan.getEmergencyPhone()));
            document.add(new Paragraph("Kayak Description"));
            document.add(new Paragraph("Make: " + floatPlan.getKayakMake() + ", Model: " + floatPlan.getKayakModel()));
            document.add(new Paragraph("Length: " + floatPlan.getKayakLength() + ", Color: " + floatPlan.getKayakColor()));
            document.add(new Paragraph("Safety Equipment Notes: " + floatPlan.getSafetyEquipmentNotes()));
            document.add(new Paragraph("Vehicle Information"));
            document.add(new Paragraph("Make: " + floatPlan.getVehicleMake() + ", Model: " + floatPlan.getVehicleModel()));
            document.add(new Paragraph("Trip Details"));
            document.add(new Paragraph("Departure: " + floatPlan.getDepartureDate() + " " + floatPlan.getDepartureTime()));
            document.add(new Paragraph("Put-In: " + floatPlan.getPutInLocation()));
            document.add(new Paragraph("Take-Out: " + floatPlan.getTakeOutLocation()));
            document.add(new Paragraph("Return: " + floatPlan.getReturnTime()));
            document.add(new Paragraph("Notes: " + floatPlan.getTripNotes()));
            document.close();
        }
        catch (Exception e)
        {
            Toast.makeText(requireContext(), "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private interface UploadCallback
    {
        void onComplete(String downloadUrl);
    }

    private void uploadFloatPlanPdf(FloatPlan floatPlan, File pdfFile, UploadCallback callback)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null)
        {
            callback.onComplete(null);
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pdfRef = storageRef.child("float_plans/" + auth.getCurrentUser().getUid() + "/" + pdfFile.getName());
        pdfRef.putFile(android.net.Uri.fromFile(pdfFile))
                .addOnProgressListener(taskSnapshot ->
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    Toast.makeText(requireContext(), "Uploading: " + (int) progress + "%", Toast.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(taskSnapshot -> pdfRef.getDownloadUrl().addOnSuccessListener(uri -> callback.onComplete(uri.toString())))
                .addOnFailureListener(e ->
                {
                    Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onComplete(null);
                });
    }

    private interface MetadataCallback
    {
        void onComplete(String floatPlanId);
    }

    private void saveFloatPlanMetadata(FloatPlan floatPlan, MetadataCallback callback)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("float_plans")
                .add(floatPlan.toMap())
                .addOnSuccessListener(documentReference -> callback.onComplete(documentReference.getId()))
                .addOnFailureListener(e ->
                {
                    Toast.makeText(requireContext(), "Failed to save metadata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onComplete(null);
                });
    }

    private void shareFloatPlan(String floatPlanId)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("float_plans").document(floatPlanId).get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    String pdfUrl = documentSnapshot.getString("pdfUrl");

                    if (pdfUrl != null)
                    {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "My Kayak Float Plan: " + pdfUrl);
                        shareIntent.setType("text/plain");
                        startActivity(Intent.createChooser(shareIntent, "Share Float Plan"));
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "No PDF URL found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error fetching float plan: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}