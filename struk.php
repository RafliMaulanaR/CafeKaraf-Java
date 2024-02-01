<?php 
   
   include "koneksi.php";

   $fungsi = new struk();

   $struk = $fungsi ->queryTampil("SELECT * FROM strukk WHERE id_transaksi ='$_GET[id_trans]'");
   $barang = $fungsi -> selectWhere("strukk","id_transaksi",$_GET['id_trans']);
   $total_beli = $fungsi -> selectSumWhere("strukk","harga_menu","jumlah","id_transaksi = '$_GET[id_trans]'");

 ?>


<!DOCTYPE html>
<html>
<head>
	<title></title>
	<style type="text/css">
	body
	{
	border: 1px solid black; width: 36%; text-align: center; margin-left: 35%; margin-top: 30px	
	}
		table{
	
			margin-top: 90px;
			text-align: center;
			margin-top: -40px;
	
		}
	</style>  
</head>

<body>
	<tr>
		
			<td>
			
				<p style="font-size: 30px; font-family: century-gothic;">
				<img src="Restaurant_40px.png" style="font-size: 10px;">
				 Struk CAFEKARAF
				<img src="Restaurant_40px.png" style="font-size: 10px;"> <hr>Jalan Raya Seseupan Ciawi Telp : 087874163076</p></p>
			</td>
		</tr>
<table  cellpadding="10"  cellspacing="0" style="margin-top: -30px;">
<hr>
<thead>
	<tr>	
		<th>NO</th>
		<th>ID KASIR</th>
		<th>ID Transaksi</th>
		<th>Nama Menu</th>
		<th>Harga Menu</th>
		<th>Jumlah Beli</th>

	</tr>	
</thead>
<tbody>

<?php $no =1; foreach ($struk as $str) : ?>

	<tr>
		<td><?= $no?></td>
		<td><?= $str['id_kasir']?></td>
		<td><?= $str['id_transaksi']?></td>
		<td><?= $str['nama_menu']?></td>
		<td><?= number_format($str['harga_menu'])?></td>
		<td><?= $str['jumlah_beli']?></td>


	</tr>
	<br>
	

</tbody>
<?php $no++; endforeach ?>
<tfoot>
	<tr>
		<th colspan="3" ></th>
		<th>Total Harga</th>
		<th>Bayar </th>
		<th>Kembalian</th>
	</tr>
	<tr>
		<td colspan="3"></td>
		<td><?= number_format($barang['total_beli'])?></td>
		<td><?= number_format($barang['bayar'])?></td>
		<td><?= number_format($barang['kembalian'])?></td>
	</tr>
</tfoot>

</table>

</body>
</html>