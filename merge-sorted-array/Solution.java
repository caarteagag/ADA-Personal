import java.util.Arrays;

/**
 * 88. Merge Sorted Array
 * https://leetcode.com/problems/merge-sorted-array/
 *
 * Enfoque: fusión (merge) desde el final, in-place.
 *
 * Esta es la fase de MERGE de Merge Sort, sin la fase de división: ya
 * llegan dos corridas ordenadas en no decreciente (los primeros m
 * valores de nums1, y los n valores de nums2). Solo falta intercalarlas.
 *
 * Por qué desde el final y no desde el índice 0:
 * Si se fusionara hacia adelante, escribir en nums1[0] pisaría un valor
 * de nums1 que todavía no se ha consumido y habría que respaldarlo en un
 * arreglo auxiliar (O(m + n) de espacio extra). En cambio, la cola de
 * nums1 (las posiciones m .. m+n-1) es basura: son ceros de relleno.
 * Esas n casillas libres son exactamente las que se necesitan, así que
 * se escribe de derecha a izquierda desde m+n-1 y nunca se sobrescribe
 * un valor pendiente. Prueba informal: en cada paso quedan por colocar
 * (i+1) + (j+1) elementos y el cursor de escritura está en k = i+j+1,
 * es decir, siempre a la derecha de i, el último dato vivo de nums1.
 *
 * Invariante: en cada iteración se compara la COLA de cada corrida
 * (nums1[i] y nums2[j]) y se escribe el MAYOR en nums1[k]. Como ambas
 * corridas están ordenadas, el mayor global de lo que queda es
 * necesariamente uno de esos dos; por eso basta una comparación por
 * elemento y la fusión es lineal.
 *
 * Por qué esta familia y no un sort comparativo:
 * La información de "ya está ordenado" no se puede desaprovechar.
 * Concatenar y llamar a Arrays.sort() costaría O((m+n) log(m+n))
 * comparaciones; la fusión aprovecha el orden previo y baja a O(m+n),
 * que es óptimo porque hay que tocar cada uno de los m+n elementos.
 *
 * Complejidad:
 *   Tiempo:  O(m + n) -> cada elemento de nums1 y de nums2 se escribe
 *            exactamente una vez; cada iteración avanza un puntero.
 *   Espacio: O(1) adicional -> solo los tres índices i, j, k. Cumple el
 *            follow-up de LeetCode (in-place).
 */
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;     // cola de la corrida válida de nums1
        int j = n - 1;     // cola de nums2
        int k = m + n - 1; // posición de escritura, desde el final

        // Mientras queden elementos en AMBAS corridas, se compara y se
        // escribe el mayor de las dos colas.
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k] = nums1[i];
                i--;
            } else {
                nums1[k] = nums2[j];
                j--;
            }
            k--;
        }

        // Si se acabó nums1, hay que bajar lo que queda de nums2.
        // (Si se acabó nums2 no se hace nada: lo que queda de nums1 ya
        // está en su lugar definitivo, en las posiciones 0..i.)
        while (j >= 0) {
            nums1[k] = nums2[j];
            j--;
            k--;
        }
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[] a = {1, 2, 3, 0, 0, 0};
        sol.merge(a, 3, new int[]{2, 5, 6}, 3);
        System.out.println(Arrays.toString(a)); // [1, 2, 2, 3, 5, 6]

        int[] b = {1};
        sol.merge(b, 1, new int[]{}, 0);
        System.out.println(Arrays.toString(b)); // [1]

        int[] c = {0};
        sol.merge(c, 0, new int[]{1}, 1);
        System.out.println(Arrays.toString(c)); // [1]

        // Caso donde nums2 se agota primero (no entra al segundo while).
        int[] d = {4, 5, 6, 0, 0, 0};
        sol.merge(d, 3, new int[]{1, 2, 3}, 3);
        System.out.println(Arrays.toString(d)); // [1, 2, 3, 4, 5, 6]
    }
}
