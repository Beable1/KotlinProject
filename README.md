**Kelime ezberleme uygulaması.**

Kotlin ile yazılan bu program kullacının 6 tekrarlı öğrenme prensibi ile kendi girdiği kelimeleri ezberlemesini sağlar.

Kullanıcı kayıt olduktan sonra mail doğrulaması yapması gerekmektedir.

Doğrulama yapmayan kullanıcılar uygulamaya giremez.

Kullanıcı çıkış yapmadığı sürece bir daha giriş yapmasına gerek yoktur.

Kullanıcı şifresini unuttuğunda Şifremi Unuttum ekranından şifre yenileme mailini gönderebilmektedir.

Kelimenin ingilizcesi, kelimenin türkçesi, kelimenin ingilizcesinin örnek cümle içerisinde geçtiği 3 cümle ve kelimenin fotoğrafı kullanıcıya quiz içerisinde sorulmaktadır.

Kullanıcı soruyu doğru bilirse streak sayısı 1 e çıkar ve ertesi gün bir daha sorulur.
Kullanıcı soruyu ertesi gün doğru  bilirse streak sayısı 2 ye çıkar ve bir hafta sonra sorulur.
Kullanıcı soruyu bir hafta sonra  bilirse streak sayısı 3 e çıkar ve bir ay sonra sorulur.
Kullanıcı soruyu bir ay sonra  bilirse streak sayısı 4 e çıkar ve üç ay sonra bir daha sorulur.
Kullanıcı soruyu üç ay sonra  bilirse streak sayısı 5 e çıkar ve altı ay sonra bir daha sorulur.
Kullanıcı soruyu altı ay sonra  bilirse streak sayisi 6 ya çıkar ve bir yıl sonra bir daha sorulur.
Kullanıcı soruyu bir yıl sonra soruyu bilirse streak sayısı 7 ye çıkar ve kullanıcının artık bu kelimeyi öğrendiği varsayılarak bir daha sorulmaz.

Kullanıcı eğer yukarıdaki adımlardan herhangi birinde yanlış cevap verirse streak sıfırlanır ve öğrenme süreci başa döner.

Kullanıcınından kelime ekleme ekranından her kelime için ingilizcesi, türkçesi, üç tane örnek cümlesi ve kelimenin örnek bir resmi girilmesi beklenmektedir.

Kullanıcı İstatistikler ekranından quizde karşısına çıkan her kelime için doğru bilme sayısı, yanlış bilme sayısı, en uzun streak sayılarına bakabilmektedir.

Kullanıcı Anasayfadaki Ayarlar Menüsünden kelime sıklığını değiştirebilmektedir.

KULLANICININ YAPMAMASI GEREKENLER!
Hatalı bilgi girilmemesi gerekmektedir. Kayıt edilen kelimenin sonradan herhangi bir bilgisini değiştirecek bir mekanizma yoktur.


Bu uygulamada database olarak Firebase kullanılmıştır.
