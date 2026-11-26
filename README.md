# Jaga Listrik Nusantara - proyek APK
Proyek ini membungkus game (app/src/main/assets/index.html) dalam WebView, layar penuh dan offline.
Belum dikompilasi dan diuji di sini.
Cara 1: buka folder ini di Android Studio, lalu Build > Build APK(s).
Cara 2: unggah ke repo GitHub. Workflow "Build APK" menghasilkan file APK di tab Actions > Artifacts.
Font Google tidak dimuat offline, game memakai font bawaan sistem.

## Mode online (pembungkus artifact)
Saat dibuka dan ada internet, aplikasi menawarkan "Main Online" (memuat halaman artifact di claude.ai;
pemain masuk dengan akun Claude di dalam aplikasi) atau "Main Offline" (game lokal di assets/index.html).
Ubah `ONLINE_URL` di MainActivity.java bila tautan artifact berbeda. Fitur online di dalam game baru aktif
setelah dunia bersama diimplementasikan; halaman yang sama dipakai di kedua mode.

## Pembaruan otomatis mode offline
1. Unggah `index.html` game ke tempat publik (mis. GitHub Pages) dan isi `UPDATE_URL` di MainActivity.java.
2. Tiap aplikasi dibuka dengan internet, file terbaru diunduh di latar belakang dan dipakai mulai pembukaan berikutnya.
3. Versi unduhan hanya dipakai jika lebih baru dari waktu pemasangan APK, jadi APK baru tidak tertimpa file lama.
4. Mode online (artifact) selalu memakai versi terbaru yang dipublikasikan, tanpa langkah ini.
