<?php 
	
	include "koneksi.php";
	$fungsi = new struk();
	$lapo = $fungsi-> queryTampil("SELECT * FROM tb_transaksi");
	$total_beli = $fungsi -> selectSum("tb_transaksi","total","jumlah");

 ?>
 <!DOCTYPE html>
 <html>
 <head>
 	<title></title>
 	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
 </head>
 <body>
 	<p style="text-align: center; font-size: 30px;">LAPORAN CAFEKARAF</p>
 	<table border="1" cellpadding="10" cellspacing="0" class="table table-striped" style="width: 80%; text-align: center; margin-left: 200px;" > 
 <thead>
 
 	<tr>
 		<th>ID KASIR</th>
 		<th>ID TRANSAKSI</th>
 		<th>ID MENU</th>
 		<th>Nama Menu</th>
 		<th>Harga Menu</th>
 		<th>Jumlah Beli</th>
 		<th>Total</th>
 		<th>Tanggal Beli</th>
 	</tr>
 </thead>
 <tbody>
 	<?php foreach ($lapo as $lp ) : ?>
 	<tr>
 		<td><?= $lp['id_kasir']?></td>
 		<td><?= $lp['id_transaksi']?></td>
 		<td><?= $lp['id_menu']?></td>
 		<td><?= $lp['nama_menu']?></td>
 		<td><?= number_format($lp['harga_menu']) ?></td>
 		<td><?= number_format($lp['jumlah_beli']) ?></td>
 		<td><?= number_format($lp['total']) ?></td>
 		<td><?= $lp['tgl_beli']?></td>
 	</tr>
  <?php endforeach ?>
</tbody>
<tfoot>
	<tr>
		<td colspan="4"></td>
		<td colspan="2">Total Pendapatan</td>
		<td colspan="2">Rp.<?= number_format($total_beli['jumlah']) ?></td>

	</tr>
</tfoot>
</table>
 </body>
 </html>