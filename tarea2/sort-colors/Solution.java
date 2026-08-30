import java.util.Arrays;

/**
 * 75. Sort Colors
 * https://leetcode.com/problems/sort-colors/
 *
 * Enfoque: bandera holandesa (Dutch National Flag) — tres punteros, una
 * sola pasada, in-place. Es la versión "de una pasada" del Counting Sort
 * para el caso k = 3.
 *
 * Por qué NO un sort comparativo:
 * 1) El enunciado prohíbe explícitamente usar la función de sort de la
 *    librería (nums.sort() / Arrays.sort()).
 * 2) El universo de claves es diminuto: k = 3 (solo 0, 1 y 2). Con un
 *    universo acotado no hace falta comparar elementos entre sí, basta
 *    con clasificarlos por su valor, y eso se hace en tiempo lineal.
 *
 * Por qué esto no viola la cota Omega(n log n):
 * Esa cota es del MODELO DE COMPARACIONES: vale para algoritmos cuya
 * única operación sobre las claves es preguntar "a < b?". El árbol de
 * decisión de un algoritmo así tiene n! hojas y por tanto altura
 * Omega(n log n). Counting Sort / bandera holandesa no vive en ese
 * modelo: usa el VALOR de la clave como dirección (mira si nums[mid] es
 * 0, 1 o 2 y lo manda a una zona fija), no compara dos elementos del
 * arreglo entre sí. Al no ser un algoritmo de comparaciones, la cota
 * inferior no le aplica, y puede alcanzar O(n + k).
 *
 * Invariante de los tres punteros (todo índice queda en una zona):
 *   nums[0 .. low-1]      -> ya son 0   (zona roja, cerrada)
 *   nums[low .. mid-1]    -> ya son 1   (zona blanca, cerrada)
 *   nums[mid .. high]     -> sin revisar
 *   nums[high+1 .. n-1]   -> ya son 2   (zona azul, cerrada)
 * El bucle termina cuando mid > high, es decir, cuando la zona sin
 * revisar queda vacía.
 *
 * Detalle importante del caso nums[mid] == 2:
 * al intercambiar con high se trae un elemento DESCONOCIDO (venía de la
 * zona sin revisar), así que mid NO avanza: hay que volver a examinar esa
 * misma posición. En cambio, con nums[mid] == 0 el swap trae un elemento
 * de nums[low], que ya fue revisado y solo puede ser un 1, así que mid sí
 * puede avanzar. Aun así el algoritmo es de UNA pasada: cada iteración
 * decrementa high o incrementa mid, y ambos se mueven a lo sumo n veces
 * en total, por lo que el bucle hace como máximo O(n) iteraciones.
 *
 * Complejidad:
 *   Tiempo:  O(n + k) con k = 3 constante -> O(n). Cada iteración cierra
 *            un índice (mid++ o high--), así que hay a lo sumo n pasos.
 *   Espacio: O(1) adicional -> solo los tres punteros low, mid, high.
 *            Cumple el follow-up (una pasada y memoria constante).
 */
class Solution {
    public void sortColors(int[] nums) {
        int low = 0;               // frontera derecha de los 0
        int mid = 0;               // elemento actual bajo revisión
        int high = nums.length - 1; // frontera izquierda de los 2

        while (mid <= high) {
            if (nums[mid] == 0) {
                // Rojo: va a la zona izquierda. Lo que vuelve de low ya
                // fue revisado y solo puede ser un 1 -> mid avanza.
                swap(nums, low, mid);
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                // Blanco: ya está en la zona del medio, solo se avanza.
                mid++;
            } else {
                // Azul: va a la zona derecha. Lo que vuelve de high NO se
                // ha revisado -> mid se queda para examinarlo.
                swap(nums, mid, high);
                high--;
            }
        }
    }

    private void swap(int[] nums, int a, int b) {
        int tmp = nums[a];
        nums[a] = nums[b];
        nums[b] = tmp;
    }

    /**
     * Alternativa de DOS pasadas (counting sort explícito con k = 3).
     * No se usa en la entrega de LeetCode (se entregó la de una pasada),
     * se deja para comparar: misma cota O(n + k) en tiempo y O(k) = O(1)
     * en espacio, porque los contadores son solo 3 variables.
     */
    public void sortColorsCounting(int[] nums) {
        int c0 = 0, c1 = 0, c2 = 0;

        // Pasada 1: contar ocurrencias de cada clave del universo.
        for (int v : nums) {
            if (v == 0) c0++;
            else if (v == 1) c1++;
            else c2++;
        }

        // Pasada 2: reescribir el arreglo por bloques.
        int i = 0;
        while (c0-- > 0) nums[i++] = 0;
        while (c1-- > 0) nums[i++] = 1;
        while (c2-- > 0) nums[i++] = 2;
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[] a = {2, 0, 2, 1, 1, 0};
        sol.sortColors(a);
        System.out.println(Arrays.toString(a)); // [0, 0, 1, 1, 2, 2]

        int[] b = {2, 0, 1};
        sol.sortColors(b);
        System.out.println(Arrays.toString(b)); // [0, 1, 2]

        int[] c = {0};
        sol.sortColors(c);
        System.out.println(Arrays.toString(c)); // [0]

        int[] d = {2, 2, 2, 0, 0, 0};
        sol.sortColorsCounting(d);
        System.out.println(Arrays.toString(d)); // [0, 0, 0, 2, 2, 2]
    }
}
