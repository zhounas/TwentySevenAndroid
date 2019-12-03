<?php
$dbc=mysqli_connect("localhost","root","root","twentyseven");

if (isset($_GET['android_id']) AND isset($_GET['logout'])){
	$android_id=$_GET['android_id'];
	if(mysqli_query($dbc,"UPDATE utilisateurs SET status= '0',android_id='' WHERE android_id='$android_id' ") )
	{
		echo"Deconnexion ";
	}
}

if(isset($_GET['username']) AND isset($_GET['password']) AND isset($_GET['android_id'])){
	$username=$_GET['username'];
	$password=$_GET['password'];
	$android_id=$_GET['android_id'];
	$result=mysqli_query($dbc,"SELECT id FROM utilisateurs WHERE utilisateur= '$username' AND mot_de_passe= '$password'");
	if(mysqli_num_rows($result)==1){
		if(mysqli_query($dbc,"UPDATE utilisateurs SET status= '1',android_id='$android_id' WHERE utilisateur='$username' "))
		{
			echo"Bienvenu $username";
		}
	}else{
		echo"Error 1 ";
	}
}

if(isset($_GET['id_status'])){
	$android_id=$_GET['id_status'];
	$result=mysqli_query($dbc,"SELECT * FROM utilisateurs WHERE android_id= '$android_id'");
	if(mysqli_num_rows($result)==1){
		$row=mysqli_fetch_assoc($result);
		if($row['status']==1){
				echo"Bienvenu ".$row['utilisateur']." ";
		}else{
			echo"Not connected";
		}	
	}else{
		echo"Not connected ";
	}	
}
?>