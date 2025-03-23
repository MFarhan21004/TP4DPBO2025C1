import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Menu extends JFrame {

    public static void main(String[] args) {
        // Buat object window
        Menu window = new Menu();

        // Atur ukuran window
        window.setSize(480, 540);

        // Letakkan window di tengah layar
        window.setLocationRelativeTo(null);

        // Isi window
        window.setContentPane(window.mainPanel);

        // Ubah warna background
        window.getContentPane().setBackground(Color.white);

        // Tampilkan window
        window.setVisible(true);

        // Agar program bisa berhenti saat window ditutup
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }




    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JRadioButton SNBP;
    private JRadioButton SNBT;
    private JRadioButton Mandiri;

    public Menu() {
        // Inisialisasi ListMahasiswa
        listMahasiswa = new ArrayList<>();

        // Isi ListMahasiswa
        populateList();

        // Isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // Ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // Atur isi combo box
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel<>(jenisKelaminData));



        // Pastikan radio button hanya bisa dipilih satu dalam satu waktu
        ButtonGroup jalurMasukGroup = new ButtonGroup();
        jalurMasukGroup.add(SNBP);
        jalurMasukGroup.add(SNBT);
        jalurMasukGroup.add(Mandiri);


        // Sembunyikan button delete
        deleteButton.setVisible(false);

        // Saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // Saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex >= 0) {
                    deleteData();
                }
            }
        });

        // Saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // Simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedJalurMasuk = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();
                // Ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);

                // Set radio button berdasarkan nilai dari tabel
                if (selectedJalurMasuk.equals("SNBP")) {
                    SNBP.setSelected(true);
                } else if (selectedJalurMasuk.equals("SNBT")) {
                    SNBT.setSelected(true);
                } else if (selectedJalurMasuk.equals("Mandiri")) {
                    Mandiri.setSelected(true);
                }

                // Ubah tombol "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // Tampilkan tombol delete
                deleteButton.setVisible(true);
            }
        });
    }


    public final DefaultTableModel setTable() {
        // Tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin","Jalur Masuk"};

        // Buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        // Isi tabel dengan data dari ListMahasiswa
        for (int i = 0; i < listMahasiswa.size(); i++) {
            Object[] row = new Object[5];
            row[0] = i + 1;
            row[1] = listMahasiswa.get(i).getNim();
            row[2] = listMahasiswa.get(i).getNama();
            row[3] = listMahasiswa.get(i).getJenisKelamin();
            row[4] = listMahasiswa.get(i).getJalurMasuk();

            temp.addRow(row);
        }

        return temp;
    }


    public void insertData() {
        // Ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String jalurMasuk = getSelectedJalurMasuk(); // Mengambil data dari radio button


        // Validasi input (Pastikan semua field terisi)
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || jalurMasuk.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tambahkan data ke dalam list
        listMahasiswa.add(new Mahasiswa(nim, nama, jenisKelamin, jalurMasuk));

        // Update tabel
        mahasiswaTable.setModel(setTable());

        // Bersihkan form
        clearForm();

        // Feedback
        System.out.println("Insert berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
    }

    // Metode untuk mendapatkan jalur masuk dari radio button
    private String getSelectedJalurMasuk() {
        if (SNBP.isSelected()) {
            return "SNBP";
        } else if (SNBT.isSelected()) {
            return "SNBT";
        } else if (Mandiri.isSelected()) {
            return "Mandiri";
        }
        return ""; // Jika tidak ada yang dipilih
    }




    public void updateData() {
        // Ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String jalurMasuk = getSelectedJalurMasuk(); // Mengambil data dari radio button

        // Ubah data mahasiswa di list
        listMahasiswa.get(selectedIndex).setNim(nim);
        listMahasiswa.get(selectedIndex).setNama(nama);
        listMahasiswa.get(selectedIndex).setJenisKelamin(jenisKelamin);
        listMahasiswa.get(selectedIndex).setJalurMasuk(jalurMasuk);

        // Update tabel
        mahasiswaTable.setModel(setTable());

        // Bersihkan form
        clearForm();

        // Feedback
        System.out.println("Update berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
    }


    public void deleteData() {
        // Konfirmasi sebelum menghapus
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // Jika user memilih "YES"
        if (confirm == JOptionPane.YES_OPTION) {
            // Hapus data dari list
            listMahasiswa.remove(selectedIndex);

            // Update tabel
            mahasiswaTable.setModel(setTable());

            // Bersihkan form
            clearForm();

            // Feedback
            System.out.println("Delete berhasil!");
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } else {
            // Jika user memilih "NO"
            System.out.println("Penghapusan dibatalkan.");
        }
    }



    public void clearForm() {
        // Kosongkan semua textfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");

        // Ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // Sembunyikan button delete
        deleteButton.setVisible(false);

        // Ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }


    private void populateList() {
        listMahasiswa.add(new Mahasiswa("2203999", "Amelia Zalfa Julianti", "Perempuan", "SNBP"));
        listMahasiswa.add(new Mahasiswa("2202292", "Muhammad Iqbal Fadhilah", "Laki-laki", "SNBT"));
        listMahasiswa.add(new Mahasiswa("2202346", "Muhammad Rifky Afandi", "Laki-laki", "Mandiri"));
        listMahasiswa.add(new Mahasiswa("2210239", "Muhammad Hanif Abdillah", "Laki-laki", "SNBT"));
        listMahasiswa.add(new Mahasiswa("2202046", "Nurainun", "Perempuan", "SNBP"));
        listMahasiswa.add(new Mahasiswa("2205101", "Kelvin Julian Putra", "Laki-laki", "Mandiri"));
        listMahasiswa.add(new Mahasiswa("2200163", "Rifanny Lysara Annastasya", "Perempuan", "SNBP"));
        listMahasiswa.add(new Mahasiswa("2202869", "Revana Faliha Salma", "Perempuan", "SNBT"));
        listMahasiswa.add(new Mahasiswa("2209489", "Rakha Dhifiargo Hariadi", "Laki-laki", "Mandiri"));
        listMahasiswa.add(new Mahasiswa("2203142", "Roshan Syalwan Nurilham", "Laki-laki", "SNBP"));
        listMahasiswa.add(new Mahasiswa("2200311", "Raden Rahman Ismail", "Laki-laki", "SNBT"));
        listMahasiswa.add(new Mahasiswa("2200978", "Ratu Syahirah Khairunnisa", "Perempuan", "Mandiri"));
        listMahasiswa.add(new Mahasiswa("2204509", "Muhammad Fahreza Fauzan", "Laki-laki", "SNBP"));
        listMahasiswa.add(new Mahasiswa("2205027", "Muhammad Rizki Revandi", "Laki-laki", "SNBT"));
        listMahasiswa.add(new Mahasiswa("2203484", "Arya Aydin Margono", "Laki-laki", "Mandiri"));
        listMahasiswa.add(new Mahasiswa("2200481", "Marvel Ravindra Dioputra", "Laki-laki", "SNBP"));
        listMahasiswa.add(new Mahasiswa("2209889", "Muhammad Fadlul Hafiizh", "Laki-laki", "SNBT"));
        listMahasiswa.add(new Mahasiswa("2206697", "Rifa Sania", "Perempuan", "Mandiri"));
        listMahasiswa.add(new Mahasiswa("2207260", "Imam Chalish Rafidhul Haque", "Laki-laki", "SNBP"));
        listMahasiswa.add(new Mahasiswa("2204343", "Meiva Labibah Putri", "Perempuan", "SNBT"));
    }

}
