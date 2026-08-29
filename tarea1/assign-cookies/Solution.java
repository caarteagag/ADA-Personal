import java.util.Arrays;

/**
 * 455. Assign Cookies
 * https://leetcode.com/problems/assign-cookies/
 *
 * Enfoque greedy:
 * Se ordenan tanto los factores de gula g[] como los tamaños de
 * galleta s[] de menor a mayor. Luego se recorren ambos arreglos con
 * dos punteros, intentando siempre satisfacer primero al niño MENOS
 * exigente con la galleta MÁS PEQUEÑA que le alcance.
 *
 * Criterio local: para el niño actual (el más fácil de contentar que
 * queda), se busca la galleta más chica disponible que sea >= a su
 * gula. Si la galleta actual no le alcanza, esa galleta es demasiado
 * pequeña para CUALQUIER niño restante (porque los niños están
 * ordenados de menor a mayor exigencia), así que se descarta y se
 * prueba con la siguiente galleta, más grande.
 *
 * Por qué es greedy y no hay que "retractarse":
 * Si a un niño fácil de contentar se le asigna una galleta más
 * grande de la que necesita, se desperdicia esa galleta grande y un
 * niño más exigente podría quedarse sin ninguna. Asignar la galleta
 * más pequeña que alcance nunca perjudica la solución óptima (se
 * puede demostrar por intercambio/exchange argument), así que la
 * decisión local es también la mejor decisión global.
 *
 * Complejidad:
 *   Tiempo:  O(n log n + m log m) -> dominado por el ordenamiento de
 *            g (tamaño n) y s (tamaño m). La pasada con dos punteros
 *            es O(n + m).
 *   Espacio: O(1) adicional (sin contar el espacio que use el sort
 *            interno del lenguaje), ya que no se usan estructuras
 *            auxiliares más allá de variables de control.
 */
class Solution {
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);

        int i = 0; // puntero para niños (g)
        int j = 0; // puntero para galletas (s)
        int satisfied = 0;

        while (i < g.length && j < s.length) {
            if (s[j] >= g[i]) {
                // La galleta j alcanza para el niño i -> se asignan
                satisfied++;
                i++;
                j++;
            } else {
                // La galleta j es muy chica para cualquier niño que
                // quede (todos >= g[i]) -> se descarta y se prueba
                // con la siguiente galleta.
                j++;
            }
        }

        return satisfied;
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.findContentChildren(new int[]{1, 2, 3}, new int[]{1, 1})); // 1
        System.out.println(sol.findContentChildren(new int[]{1, 2}, new int[]{1, 2, 3}));  // 2
    }
}
