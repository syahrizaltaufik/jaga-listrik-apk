# Jaga Listrik Nusantara - proyek APK

Proyek ini membungkus game (`app/src/main/assets/index.html`) dalam WebView, layar penuh dan bisa dimainkan offline.

## Cara membuat APK

1. Unggah proyek ini ke repo GitHub (`jaga-listrik-apk`).
2. Buka tab **Actions**, pilih workflow **Build APK**, lalu tunggu sampai hijau.
3. Unduh file APK dari bagian **Artifacts** pada run yang berhasil.

Atau buka folder ini di Android Studio, lalu pilih Build > Build APK(s).
Setiap commit memicu build baru, jadi merah di tengah proses unggah beberapa file itu wajar. Yang dipakai adalah build terakhir.

## Mode permainan

Aplikasi selalu memuat game lokal (`assets/index.html`), jadi bisa dimainkan tanpa internet.

- **Main Offline:** perusahaan dikelola sendiri, dengan 3 slot simpanan di perangkat.
- **Main Online:** dunia bersama lewat akun (email dan kata sandi) dan kode ruangan. Perlu internet. Layanannya Firebase, yang dikonfigurasi di dalam `index.html` (bukan di proyek APK ini).

## Pembaruan otomatis

1. Unggah `index.html` game terbaru ke repo GitHub Pages (`jaga-listrik`). Alamatnya `https://syahrizaltaufik.github.io/jaga-listrik/`.
2. Alamat itu diisi di `UPDATE_URL` pada `MainActivity.java`.
3. Setiap aplikasi dibuka dengan internet, versi terbaru diunduh di latar belakang dan baru dipakai pada pembukaan berikutnya. Jadi buka aplikasi dua kali setelah ada pembaruan.
4. Versi unduhan hanya dipakai jika lebih baru dari waktu pemasangan APK, sehingga APK baru tidak tertimpa file lama.

APK hanya perlu dibangun ulang bila ada perubahan di sisi Android (ikon, nama, izin).

## Catatan

- **Save offline** tersimpan di aplikasi dan terpisah dari save di peramban web. Untuk memindahkannya, pakai **Ekspor / Impor save** di menu utama game.
- **Font Google** tidak dimuat offline, game memakai font bawaan sistem.
- **Ikon aplikasi** ada di `app/src/main/res` (`drawable/ic_launcher_foreground.xml`, `mipmap-anydpi-v26`, `values/colors.xml`).
