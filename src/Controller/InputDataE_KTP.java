package Controller;

import javax.swing.*;
import java.io.File;
import java.awt.*;
import org.jdatepicker.impl.*;
import java.util.Properties;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import Model.DateLabelFormatter;

public class InputDataE_KTP {
    private JFrame frameInput;
    private JFrame frameKTP;
    private JTextField nikField, namaField, tempatLahirField, alamatField, rtRwField, kelDesaField, kecamatanField, berlakuField, kotaField, dateMade;
    private JComboBox<String> agamaComboBox, statusComboBox;
    private JCheckBox swastaCheck, pnsCheck, wiraswastaCheck, akademisiCheck, pengangguranCheck;
    private JRadioButton priaButton, wanitaButton, golDarA, golDarB, golDarO, golDarAB, wniButton, wnaButton;
    private JTextField wnaField; // di-hide dulu, jika di-klik WNA baru muncul
    private JButton submitButton, fotoButton, ttdButton;
    private ButtonGroup genderGroup, kewarganegaraanGroup, golDarahGroup;
    private JDatePickerImpl tanggalLahir;
    private JLabel fotoPanel, tandaTanganPanel;
    private File selectedFoto, selectedTTD;

    // Ukuran frame dan margin
    private static final int FRAME_WIDTH = 570;
    private static final int FRAME_HEIGHT = 420;
    private static final int MARGIN = 10;
    private static final int LINE_SPACING = 22;

    // Ukuran font
    private static final int TITLE_FONT_SIZE = 25;
    private static final int LABEL_FONT_SIZE = 12;
    private static final int NIK_FONT_SIZE = 15; // Ukuran font khusus untuk NIK

    // Ukuran foto
    private static final int FOTO_WIDTH = 170;
    private static final int FOTO_HEIGHT = 200;

    // Kolom data
    private static final int COLON_WIDTH = 10; // Lebar untuk ":"
    private static final int COLUMN_MARGIN = 155; // Margin kolom kanan untuk value

    // Constructor
    public InputDataE_KTP() {
        // Frame Input Data Penduduk
        frameInput = new JFrame("Form Input Data Penduduk");
        frameInput.setSize(850, 600);
        frameInput.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameInput.setLocationRelativeTo(null);
        frameInput.setLayout(new BorderLayout());

        JLabel judulForm = new JLabel("Form Input Data Penduduk", SwingConstants.CENTER);
        judulForm.setFont(new Font("Arial", Font.BOLD, 27));
        frameInput.add(judulForm, BorderLayout.NORTH);

        // Panel Data Input
        JPanel panelDataInput = new JPanel(new GridLayout(21, 2));
        panelDataInput.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Input Fields
        addInputField(panelDataInput, "NIK:", nikField = new JTextField());
        addInputField(panelDataInput, "Nama:", namaField = new JTextField());
        addInputField(panelDataInput, "Tempat Lahir:", tempatLahirField = new JTextField());
        addInputField(panelDataInput, "Tanggal Lahir:", tanggalLahir = createDatePicker(tanggalLahir));
        addInputField(panelDataInput, "Jenis Kelamin:", createGenderPanel());
        addInputField(panelDataInput, "Gol. Darah:", createGolDarahPanel());
        addInputField(panelDataInput, "Alamat:", alamatField = new JTextField());
        addInputField(panelDataInput, "RT/RW:", rtRwField = new JTextField());
        addInputField(panelDataInput, "Kelurahan/Desa:", kelDesaField = new JTextField());
        addInputField(panelDataInput, "Kecamatan:", kecamatanField = new JTextField());
        addInputField(panelDataInput, "Agama:", agamaComboBox = createComboBox(
                new String[] { "Pilih Agama", "Islam", "Kristen", "Katolik", "Hindu", "Buddha", "Konghucu" }));
        addInputField(panelDataInput, "Status Perkawinan:", statusComboBox = createComboBox(
                new String[] { "Pilih Status Perkawinan", "Belum Menikah", "Menikah", "Janda/Duda" }));
        addInputField(panelDataInput, "Pekerjaan:", createPekerjaanPanel());

        // Membuat radio button kewarganegaraan
        panelDataInput.add(new JLabel("Kewarganegaraan: "));
        wniButton = new JRadioButton("WNI");
        wniButton.setActionCommand("WNI");
        wnaButton = new JRadioButton("WNA");
        wnaButton.setActionCommand("WNA");

        kewarganegaraanGroup = new ButtonGroup();
        kewarganegaraanGroup.add(wniButton);
        kewarganegaraanGroup.add(wnaButton);

        JPanel kewarganegaraanPanel = new JPanel();
        kewarganegaraanPanel.add(wniButton);
        kewarganegaraanPanel.add(wnaButton);

        panelDataInput.add(kewarganegaraanPanel);

        wnaField = new JTextField("Negara asal");
        // wnaField.setEnabled(false);
        wnaField.setVisible(false);
        wnaButton.addActionListener(e -> wnaField.setVisible(true));
        wniButton.addActionListener(e -> wnaField.setVisible(false));

        // Menambahkan elemen kosong
        panelDataInput.add(new JPanel());
        panelDataInput.add(wnaField);
        // Tombol untuk input foto
        panelDataInput.add(new JLabel("Foto:"));
        fotoButton = new JButton("Pilih Foto");
        JTextField fotoLabelName = new JTextField();
        fotoLabelName.setEditable(false);

        JPanel fotoPanelInput = new JPanel();
        fotoPanelInput.setLayout(new GridLayout(1, 2));
        fotoPanelInput.add(fotoLabelName);
        fotoPanelInput.add(fotoButton);

        panelDataInput.add(fotoPanelInput);

        // Action listener untuk tombol foto
        fotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFoto = pilihFileGambar();
                fotoLabelName.setText(selectedFoto.getName());
            }
        });

        // Tombol untuk input tanda tangan
        panelDataInput.add(new JLabel("Tanda Tangan:"));
        ttdButton = new JButton("Pilih Tanda Tangan");

        JTextField ttdLabelName = new JTextField("");
        ttdLabelName.setEditable(false);

        JPanel ttdPanel = new JPanel();
        ttdPanel.setLayout(new GridLayout(1, 2));
        ttdPanel.add(ttdLabelName);
        ttdPanel.add(ttdButton);

        panelDataInput.add(ttdPanel);

        // Action listener untuk tombol tanda tangan
        ttdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTTD = pilihFileGambar();
                ttdLabelName.setText(selectedFoto.getName());
            }
        });

        addInputField(panelDataInput, "Berlaku Hingga:", berlakuField = new JTextField());
        addInputField(panelDataInput, "Kota Pembuatan:", kotaField = new JTextField());
        addInputField(panelDataInput, "Tanggal Pembuatan KTP:", dateMade = createDateField());


        // Tombol Submit
        submitButton = new JButton("Insert Data");
        submitButton.setPreferredSize(new Dimension(400, 35));
        frameInput.add(submitButton, BorderLayout.SOUTH);
        // Submit Action
        submitButton.addActionListener(e -> handleSubmit());

        // Untuk membuat padding dalam
        panelDataInput.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        // Untuk membuat scroll
        JScrollPane scrollPanel = new JScrollPane(panelDataInput);
        scrollPanel.setBorder(null);
        frameInput.add(scrollPanel);
        frameInput.setVisible(true);
    }

    // Fungsi untuk menambahkan label dan komponen input ke panel
    private void addInputField(JPanel panel, String label, JComponent inputComponent) {
        panel.add(new JLabel(label));
        panel.add(inputComponent);
    }

    // Fungsi untuk membuat Date Field yang tidak bisa diedit
    private JTextField createDateField() {
        JTextField dateField = new JTextField(String.valueOf(formatDate(new Date())));
        dateField.setEditable(false);
        return dateField;
    }

    // Fungsi untuk membuat ComboBox
    private JComboBox<String> createComboBox(String[] options) {
        return new JComboBox<>(options);
    }

    // Fungsi untuk membuat panel Gender
    private JPanel createGenderPanel() {
        priaButton = new JRadioButton("Pria");
        priaButton.setActionCommand("PRIA");
        wanitaButton = new JRadioButton("Wanita");
        wanitaButton.setActionCommand("WANITA");

        genderGroup = new ButtonGroup();
        genderGroup.add(priaButton);
        genderGroup.add(wanitaButton);

        JPanel genderPanel = new JPanel();
        genderPanel.add(priaButton);
        genderPanel.add(wanitaButton);

        return genderPanel;
    }

    // Fungsi untuk membuat panel Golongan Darah
    private JPanel createGolDarahPanel() {
        golDarA = new JRadioButton("A");
        golDarA.setActionCommand("A");
        golDarB = new JRadioButton("B");
        golDarB.setActionCommand("B");
        golDarO = new JRadioButton("O");
        golDarO.setActionCommand("O");
        golDarAB = new JRadioButton("AB");
        golDarAB.setActionCommand("AB");

        golDarahGroup = new ButtonGroup();
        golDarahGroup.add(golDarA);
        golDarahGroup.add(golDarB);
        golDarahGroup.add(golDarO);
        golDarahGroup.add(golDarAB);

        JPanel golDarPanel = new JPanel();
        golDarPanel.add(golDarA);
        golDarPanel.add(golDarB);
        golDarPanel.add(golDarO);
        golDarPanel.add(golDarAB);

        return golDarPanel;
    }

    // Fungsi untuk membuat panel Pekerjaan
    private JPanel createPekerjaanPanel() {
        swastaCheck = new JCheckBox("Karyawan Swasta");
        pnsCheck = new JCheckBox("PNS");
        wiraswastaCheck = new JCheckBox("Wiraswasta");
        akademisiCheck = new JCheckBox("Akademisi");
        pengangguranCheck = new JCheckBox("Pengangguran");

        pengangguranCheck.addActionListener(e -> togglePekerjaan(pengangguranCheck.isSelected()));

        JPanel pekerjaanPanel = new JPanel(new GridLayout(2, 3));
        pekerjaanPanel.add(swastaCheck);
        pekerjaanPanel.add(pnsCheck);
        pekerjaanPanel.add(wiraswastaCheck);
        pekerjaanPanel.add(akademisiCheck);
        pekerjaanPanel.add(pengangguranCheck);

        return pekerjaanPanel;
    }

    // Fungsi untuk memilih dan menyimpan file
    private File pilihFileGambar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filter hanya untuk file gambar
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "File Gambar (*.jpg, *.jpeg, *.png)";
            }
        });

        int result = fileChooser.showOpenDialog(null);

        // Jika pengguna memilih file
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    // Fungsi untuk menampilkan gambar ke JLabel
    private void tampilkanGambar(JLabel fotoPanel, File selectedFile) {
        if (selectedFile != null) {
            // Menampilkan gambar pada JLabel
            ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
            // Menyesuaikan ukuran gambar dengan ukuran label
            Image image = imageIcon.getImage().getScaledInstance(fotoPanel.getWidth(), fotoPanel.getHeight(),
                    Image.SCALE_SMOOTH);
            fotoPanel.setIcon(new ImageIcon(image));
        } else {
            JOptionPane.showMessageDialog(null, "Tidak ada file yang dipilih!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Untuk membuat datePicker
    private JDatePickerImpl createDatePicker(JDatePickerImpl datePicker) {
        // Properti untuk Date Picker
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        // Membuat Date Picker
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        return datePicker;
    }

    // Fungsi untuk bagian pekerjaan
    private void togglePekerjaan(boolean disable) {
        swastaCheck.setEnabled(!disable);
        pnsCheck.setEnabled(!disable);
        wiraswastaCheck.setEnabled(!disable);
        akademisiCheck.setEnabled(!disable);
    }

    // Fungsi untuk tombol submit
    private void handleSubmit() {
        if (checkDataForm()) {
        JOptionPane.showMessageDialog(frameInput, "Semua data harus diisi!", "Error",
        JOptionPane.ERROR_MESSAGE);
        } else {
        frameInput.dispose();
        JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
        showKTP();
        }
    }

    private boolean checkDataForm() {
        if (nikField.getText().isEmpty() ||
                namaField.getText().isEmpty() ||
                tempatLahirField.getText().isEmpty() ||
                alamatField.getText().isEmpty() ||
                rtRwField.getText().isEmpty() ||
                kelDesaField.getText().isEmpty() ||
                kecamatanField.getText().isEmpty() ||
                berlakuField.getText().isEmpty() ||
                kotaField.getText().isEmpty() ||
                agamaComboBox.getSelectedItem().toString().equalsIgnoreCase("Pilih Agama") ||
                statusComboBox.getSelectedItem().toString().equalsIgnoreCase("Pilih Status Perkawinan") ||

                (!swastaCheck.isSelected() && !pnsCheck.isSelected() && !wiraswastaCheck.isSelected()
                        && !akademisiCheck.isSelected() && !pengangguranCheck.isSelected())
                ||

                (!priaButton.isSelected() && !wanitaButton.isSelected()) ||

                (!golDarA.isSelected() && !golDarAB.isSelected() && !golDarB.isSelected() && !golDarO.isSelected()) ||

                (!wniButton.isSelected() && !wnaButton.isSelected())) {

            if (wnaButton.isSelected() && wnaField.getText().isEmpty()) {
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    private String getDataPekerjaan() {
        String data = "";
        if (swastaCheck.isSelected()) {
            data += swastaCheck.getText() + ", ";
        }
        if (pnsCheck.isSelected()) {
            data += pnsCheck.getText() + ", ";
        }
        if (wiraswastaCheck.isSelected()) {
            data += wiraswastaCheck.getText() + ", ";
        }
        if (akademisiCheck.isSelected()) {
            data += akademisiCheck.getText() + ", ";
        }
        if (pengangguranCheck.isSelected()) {
            data = pengangguranCheck.getText();
        }

        return data.replaceFirst(", $", "");
    }

    private String getDataKewarganegaraan() {
        if (wnaButton.isSelected()) {
            return wnaButton.getActionCommand() + " (" + wnaField.getText() + ")";
        }
        return wniButton.getActionCommand();
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    private void showKTP() {
        // Membuat frame utama
        frameKTP = new JFrame("KTP Display");
        frameKTP.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frameKTP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameKTP.setResizable(false);
        frameKTP.setLocationRelativeTo(null);

        // Panel utama
        JPanel panelUtama = new JPanel(null);
        panelUtama.setBackground(new Color(173, 216, 230)); // Warna biru muda

        // Variabel posisi awal
        int currentY = MARGIN;

        // Label untuk teks "PROVINSI DKI JAKARTA"
        currentY = tambahLabel(panelUtama, "Republik Harapan Bangsa", TITLE_FONT_SIZE, currentY, MARGIN, true) + 30;

        // Data KTP
        // Data tanggal Lahir
        Date tglLahir = (Date) ((UtilDateModel) (tanggalLahir.getModel())).getValue();
        String formattedTglLahir = "";
        if (tglLahir != null) {
            formattedTglLahir = formatDate(tglLahir);
        }

        String[][] data = {
                { "NIK", nikField.getText().toUpperCase() },
                { "Nama", namaField.getText().toUpperCase() },
                { "Tempat/Tgl Lahir", tempatLahirField.getText().toUpperCase() + ", " + formattedTglLahir },
                { "Jenis Kelamin",
                        genderGroup.getSelection().getActionCommand() + "    Gol. Darah: "
                                + golDarahGroup.getSelection().getActionCommand() },
                { "Alamat", alamatField.getText().toUpperCase() },
                { "    RT/RW", rtRwField.getText() },
                { "    Kel/Desa", kelDesaField.getText().toUpperCase() },
                { "    Kecamatan", kecamatanField.getText().toUpperCase() },
                { "Agama", agamaComboBox.getSelectedItem().toString() },
                { "Status Perkawinan", statusComboBox.getSelectedItem().toString() },
                { "Pekerjaan", getDataPekerjaan() },
                { "Kewarganegaraan", getDataKewarganegaraan() },
                { "Berlaku Hingga", berlakuField.getText() }
        };

        // Menampilkan data
        for (String[] row : data) {
            boolean isNik = row[0].equals("NIK");
            currentY = tambahData(panelUtama, row[0], row[1], currentY, isNik ? NIK_FONT_SIZE : LABEL_FONT_SIZE);
        }

        // Panel untuk foto
        int fotoX = COLUMN_MARGIN + (FRAME_WIDTH - FOTO_WIDTH) / 2;
        fotoPanel = new JLabel();
        fotoPanel.setBackground(Color.RED); // Warna merah untuk simulasi
        fotoPanel.setBounds(fotoX, MARGIN + 55, FOTO_WIDTH, FOTO_HEIGHT);
        tampilkanGambar(fotoPanel, selectedFoto);
        panelUtama.add(fotoPanel);

        // Label untuk tanggal hari ini
        JLabel lblTempatBuat = new JLabel(kotaField.getText());
        lblTempatBuat.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE + 5));
        lblTempatBuat.setBounds(fotoX, fotoPanel.getY() + FOTO_HEIGHT + 5, FOTO_WIDTH, 20);
        lblTempatBuat.setHorizontalAlignment(SwingConstants.CENTER);
        panelUtama.add(lblTempatBuat);
        
        JLabel lblTanggalBuat = new JLabel(dateMade.getText());
        lblTanggalBuat.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
        lblTanggalBuat.setBounds(fotoX, fotoPanel.getY() + lblTempatBuat.getHeight() + FOTO_HEIGHT + 3, FOTO_WIDTH, 20);
        lblTanggalBuat.setHorizontalAlignment(SwingConstants.CENTER);
        panelUtama.add(lblTanggalBuat);

        // Panel untuk tanda tangan
        tandaTanganPanel = new JLabel();
        tandaTanganPanel.setBackground(Color.BLACK); // Simulasi area tanda tangan
        tandaTanganPanel.setBounds(fotoX, lblTempatBuat.getY() + 40, FOTO_WIDTH, 40);
        tampilkanGambar(tandaTanganPanel, selectedTTD);
        panelUtama.add(tandaTanganPanel);

        // Menambahkan panel utama ke frame
        frameKTP.add(panelUtama);
        frameKTP.setVisible(true);
    }

    // Fungsi untuk buat label teks
    private static int tambahLabel(JPanel panel, String text, int fontSize, int y, int margin, boolean center) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        if (center) {
            label.setBounds(margin, y, FRAME_WIDTH - 2 * margin, 30);
            label.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            label.setBounds(margin, y, FRAME_WIDTH - margin, 20);
        }
        panel.add(label);
        return y + 30;
    }

    // Fungsi agar data dengan format "Key : Value" rapih dan sejajar
    private static int tambahData(JPanel panel, String key, String value, int y, int fontSize) {
        int labelX = MARGIN;
        int colonX = COLUMN_MARGIN;
        int valueX = COLUMN_MARGIN + COLON_WIDTH;

        JLabel lblKey = new JLabel(key);
        lblKey.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblKey.setBounds(labelX, y, colonX - labelX, 20);
        panel.add(lblKey);

        JLabel lblColon = new JLabel(":");
        lblColon.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblColon.setBounds(colonX, y, COLON_WIDTH, 20);
        panel.add(lblColon);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblValue.setBounds(valueX, y, FRAME_WIDTH - valueX - MARGIN, 20);
        panel.add(lblValue);

        return y + LINE_SPACING; // untuk tambahin spasi antar baris
    }

}
