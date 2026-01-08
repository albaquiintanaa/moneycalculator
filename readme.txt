❖Cambios Aplicados❖

➣UI/UX Avanzada:
-Sustituí los inputs básicos por un CurrencySelector personalizado basado en JTable. Ahora permite seleccionar origen (click izquierdo) y destino (click derecho) de forma intuitiva.
-Mejoré el apartado visual implementando BorderLayout, tipografías personalizadas y cursores dinámicos.

➣Feedback de Usuario:
-Integré SoundPlayer para dar feedback auditivo en operaciones exitosas o fallidas.
-Gestioné los errores mediante Popups informativos, evitando cierres inesperados por datos inválidos.

➣Arquitectura y Clean Code:
-Implementé una integración robusta de la API de tasas de cambio con manejo de excepciones y Gson.
-Eliminé conversiones inseguras (Double.parseDouble directo). Ahora se utiliza Optional y validación previa.
-SRP: Descompuse la clase Desktop extrayendo la selección a CurrencySelector y el audio a ClipSoundPlayer, dejando la vista principal limpia.
-Patrón Command & DI: Refactoricé ExchangeMoneyCommand para recibir todas sus dependencias desde el exterior, reduciendo el acoplamiento y facilitando el testing.
-Programación Funcional: Hice uso extensivo de Streams y Lambdas para simplificar el manejo de colecciones y eventos de Swing.
