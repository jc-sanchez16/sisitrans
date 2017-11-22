Rest.
ZonaServices.
1) Dar todas las zonas.
	@get
	url:http://localhost:8080/RotonAndes/rest/zonas
2) Dar una sona respecto a su id.
	@get
	url:http://localhost:8080/RotonAndes/rest/zonas/5
3) Agregar una zona a RotndAndes.
	@post
	json: {"id":13,"abierto":"true","capacidad":38,"discapacitados":"true","especialidad":"JAPONESA"}
	url:http://localhost:8080/RotonAndes/rest/zonas?usuario=67194643&clave=5246545
4) Actualiza una zona.
	@put
	json:{"id":12,"abierto":"false","capacidad":25,"discapacitados":"true","especialidad":"JAPONESA"}
	url:http://localhost:8080/RotonAndes/rest/zonas?usuario=67194643&clave=5246545
5) Borrar una zona.
	@delete
	url:http://localhost:8080/RotonAndes/rest/zonas/12
UsuarioServices.
1) Dar todos los usuarios.
	@get
	url:http://localhost:8080/RotonAndes/rest/usuarios?usuario=12345678&clave=21435423
2) Obtener un usuario por su clave.
	@get
	url:http://localhost:8080/RotonAndes/rest/usuarios/PK?clave=2346683
3) Agregar un usuario.
	@post
	json:{"id":901030,"nombre":"ESTEBAN","edad":18,"tipo":"1"}
	url:http:http://localhost:8080/RotonAndes/rest/usuarios?usuario=12345678&claveNueva=46832&clave=21435423
4) Actualizar un usuario.
	@put
	json:{"id":901030,"nombre":"RONALDO","edad":48,"tipo":"1"}
	url:http:http://localhost:8080/RotonAndes/rest/usuarios?usuario=200817&clave=46832
5) Borrar un usuario.
	@delete
	url:http://localhost:8080/RotonAndes/rest/usuarios?id=901030&usuario=12345678&clave=21435423
6) Obtener todos los administradores.
	@get
	url:http://localhost:8080/RotonAndes/rest/usuarios/admin?usuario=12345678&clave=21435423
7) Obtener un administrador por su id.
	@get
	url:http://localhost:8080/RotonAndes/rest/usuarios/admin?usuario=12345678&clave=21435423&id=67194643
8) Obtener todos los representantes.
	@get
	url:http://localhost:8080/RotonAndes/rest/usuarios/representantes?usuario=12345678&usuarioRep=0&claveUser=21435423&claveRep=0
9) Obtener un respresentante por su id.
	@get
	url:http://localhost:8080/RotonAndes/rest/usuarios/representantes?usuario=0&usuarioRep=0&claveUser=0&claveRep=0&id=918273645&restaurante=ISLA MORADA&claveRestaurante=254542354
RestauranteServices
1) Obtener todos los restaurantes.
	@get
	url:http://localhost:8080/RotonAndes/rest/restaurantes
2) Obtener un restaurante por su nombre.
	@get
	url:http://localhost:8080/RotonAndes/rest/restaurantes/PK?nombre=DOÑA SEGUNDA
3) Agregar un restaurante.
	@post
	json:{"nombre":"RAPI FOOD","tipoComida":"RAPIDA","web":"WWW.RAPIFOOD.COM","zona":5,"administrador":918273645}
	url:http:http://localhost:8080/RotonAndes/rest/restaurantes?clave=543216789&usuario=12345678&contraseñaAd=21435423
4) Actualizar un restaurante.
	@put
	json:{"nombre":"RAPI FOOD","tipoComida":"RANCHERA","web":"WWW.RAPIFOOD123.COM","zona":7,"administrador":918273645}
	url:http://localhost:8080/RotonAndes/rest/restaurantes?usuario=12345678&contraseñaAd=21435423
5) Borrar un restaurante.
	@delete
	url:http://localhost:8080/RotonAndes/rest/restaurantes?usuario=12345678&contraseñaAd=21435423&nombre=RAPI FOOD
ProductoServices.
1) Obtener todos los productos.
	@get
	url:http://localhost:8080/RotonAndes/rest/productos
2) Obtener un producto por su nombre y restaurante.
	@get
	url:http://localhost:8080/RotonAndes/rest/productos/PK?nombre=AREPA DULCE&restaurante=ANDRES CARNE DE RES
3) Agregar un producto.
	@post
	json:{"nombre": "PATACON DULCE","restaurante": "DOÑA SEGUNDA","costo": 7500,"tipo": 4,
    "descripcionE": "PATACON DELICIOSO","descripcionEn": "SWEET PATACON", "tiempoPreparacion": 13,
    "precio": 9000,"cantidadMaxima": 230}
	url: http://localhost:8080/RotonAndes/rest/productos?restaurante=DOÑA SEGUNDA&claveRestaurante=326557978
4) Actualizar un producto.
	@put
	json:{"nombre": "PATACON DULCE","restaurante": "DOÑA SEGUNDA","costo": 4500,"tipo": 4,
    "descripcionE": "PATACON DELICIOSO","descripcionEn": "SWEET PATACON", "tiempoPreparacion": 9,
    "precio": 7000,"cantidadMaxima": 210}
	url:http://localhost:8080/RotonAndes/rest/productos?restaurante=DOÑA SEGUNDA&claveRestaurante=326557978
5) Borrar un producto.
	@delete
	url:http://localhost:8080/RotonAndes/rest/productos?restaurante=DOÑA SEGUNDA&claveRestaurante=326557978&nombre=PATACON DULCE&restauranteVerificar=DOÑA SEGUNDA
MenuSServices.
1) Obtener todos los menus.
	@get
	url:http://localhost:8080/RotonAndes/rest/menus
2) Obtener un menu por su nombre y restaurante.
	@get
	url:http://localhost:8080/RotonAndes/rest/menus/PK?nombre=EL GIGANTE&restaurante=CARNES FINAS
3) Agregar un menu.
	@post
	json:{"nombre": "EAT AND EAT","restaurante": "LAS DELICIAS", "costo": 13590,
    "precio": 20000,"tiempoPreparacion": 40,"cantidadMaxima": 100}
	url:http://localhost:8080/RotonAndes/rest/menus?restaurante=LAS DELICIAS&claveRestaurante=42365
4) Actualizar un menu.
	@put
	json:{"nombre": "EAT AND EAT","restaurante": "LAS DELICIAS", "costo": 17830,
    "precio": 25340,"tiempoPreparacion": 37,"cantidadMaxima": 205}
	url:http://localhost:8080/RotonAndes/rest/menus?restaurante=LAS DELICIAS&claveRestaurante=42365
5) Borrar un menu.
	@delete
	url:http://localhost:8080/RotonAndes/rest/menus?nombre=EAT AND EAT&restaurante=LAS DELICIAS&restauranteVerificar=LAS DELICIAS&claveRestaurante=42365
IngredientesServices.
1) Obetener todos los ingredientes.
	@get
	url:http://localhost:8080/RotonAndes/rest/ingredientes
2) Obtener un ingrediente por su nombre.
	@get
	url:http://localhost:8080/RotonAndes/rest/ingredientes/PK?nombre=LECHUGA
3) Agregar un ingredientes.
	@post
	json:{"nombre": "PEPINOS",
    "descripcionE": "PEPINOS PARA ACOMPAÑAR ENSALADAS",
    "descripcionEn": "CUCUMBERS TO ACCOMPANY SALADS"}
	url:http://localhost:8080/RotonAndes/rest/ingredientes?restaurante=LAS DELICIAS&claveRestaurante=42365
4) Borrar un ingrediente.
	@delete
	url:
OrdenServices.
1) Obetener todas las ordenes
	@get
	url:http://localhost:8080/RotonAndes/rest/ordenes/
2) Obtener una orden por su fecha.
	@get
	url:http://localhost:8080/RotonAndes/rest/ordenes/PK?mesa=1&fecha=2017/10/31 11:31

RequerimientoServices.
1) Registrar equivalencias ingredientes.
	@post
	json:["LECHUGA","PAPA SALADA"]
	url:http://localhost:8080/RotonAndes/rest/RF/11?clave=256378567
2) Registrar equivalencias productos.
	@post
	json:["PLATANITOS DULCES","AREPA DULCE"]
	url: http://localhost:8080/RotonAndes/rest/RF/12?clave=256378567&restaurante=ANDRES CARNE DE RES
3) Surtir restaurante.
	@put
	url:http://localhost:8080/RotonAndes/rest/RF/13?clave=75866857&restaurante=LA PARRILLA
4) Registrar Pedido
	@post
	json:{
	"productos":["PLATANITOS DULCES;ANDRES CARNE DE RES;SALSA,JUGO,SIN SAL:;12345678","NUNCA TERMINARAS;EL GLOTON;SIN SAL:PAPAS CRIOLLAS:::::FIN;87654321"],
	"usuarios" :[12345678,87654321]
}
	url:http://localhost:8080/RotonAndes/rest/RF/15?mesa=1
5) Registrar servicio.
	@put
	url:http://localhost:8080/RotonAndes/rest/RF/16?clave=254542354&restaurante=ISLA MORADA&fecha=2017/10/31 11:31&mesa=1
6) Cancelar servicio.
	@delete
	url:http://localhost:8080/RotonAndes/rest/RF/17?clave=254542354&restaurante=ISLA MORADA&fecha=2017/10/31 11:31&mesa=1
7) Consultar consumo.
	@get
	url:http://localhost:8080/RotonAndes/rest/RF/C7?clave=21435423&peticion=93627524&usuario=12345678
8) Consultar Pedidos.
	@get
	url:http://localhost:8080/RotonAndes/rest/RF/C8?clave=21435423&usuario=12345678

c9:

http://localhost:8080/RotonAndes/rest/RF/C9?clave=454&usuario=2117&restaurante=bieiggvsqyybdjddzvwv&fechaI=2206/01/20 00:00&fechaF=2206/01/20 23:59&order=&group

c10:

http://localhost:8080/RotonAndes/rest/RF/C10?clave=454&usuario=2117&restaurante=bieiggvsqyybdjddzvwv&fechaI=2206/01/20 00:00&fechaF=2206/01/20 23:59&order=&group
















































	