/**
 * 416. Partition Equal Subset Sum
 * https://leetcode.com/problems/partition-equal-subset-sum/
 *
 * Enfoque: programación dinámica, mochila 0/1 (subset sum) con arreglo 1D.
 *
 * Reducción:
 *   Sea S la suma total. Si S es impar no se puede partir en dos mitades
 *   iguales -> false. Si es par, basta con saber si existe un subconjunto
 *   que sume EXACTAMENTE W = S / 2: los números que no entran forman la
 *   otra mitad, que también suma W.
 *
 * Estado:
 *   dp[w] = true si existe un subconjunto de los números ya considerados
 *           cuya suma es exactamente w.
 *
 * Caso base:
 *   dp[0] = true (el subconjunto vacío suma 0); dp[w] = false para w > 0
 *   antes de considerar cualquier número.
 *
 * Recurrencia (al considerar el número num):
 *   dp[w] = dp[w] OR dp[w - num],  para w >= num
 *   "O ya se podía armar w sin num, o armo w - num sin num y le agrego num".
 *
 * 0/1 (cada número a lo más una vez):
 *   w se recorre de W HACIA 0. Así, cuando se lee dp[w - num] (posición más
 *   a la izquierda), todavía tiene el valor de la fila anterior, es decir,
 *   SIN haber usado num. Si se recorriera hacia adelante, dp[w - num] ya
 *   podría incluir num y se usaría el mismo número varias veces: eso sería
 *   la mochila no acotada del ejercicio 1 (Coin Change), no este problema.
 *
 * Por qué no fuerza bruta:
 *   Enumerar los 2^n subconjuntos con n hasta 200 es inviable; la tabla
 *   solo tiene W + 1 estados y se actualiza n veces.
 *
 * Complejidad (n = nums.length, W = S / 2):
 *   Tiempo:  Θ(n · W) -> n números, cada uno recorre W .. num.
 *            Pseudo-polinomial en W (con las cotas de LeetCode,
 *            W <= 200 · 100 / 2 = 10 000).
 *   Espacio: Θ(W) -> arreglo 1D dp de tamaño W + 1 (en lugar de la matriz
 *            n × W, porque cada fila solo depende de la anterior).
 */
class Solution {
    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int num : nums) total += num;
        if (total % 2 != 0) return false;

        int W = total / 2;
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;

        for (int num : nums) {
            for (int w = W; w >= num; w--) {        // hacia atrás: cada número una sola vez
                dp[w] = dp[w] || dp[w - num];
            }
            if (dp[W]) return true;                 // ya se llenó la mitad exacta
        }
        return dp[W];
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.canPartition(new int[]{1, 5, 11, 5})); // true  ({11} y {1,5,5})
        System.out.println(sol.canPartition(new int[]{1, 2, 3, 5}));  // false (S = 11 impar)
        // false: S = 12, W = 6 no se arma. Recorriendo w hacia adelante daría
        // true por usar el 3 dos veces (3 + 3): ese es el error de reusar.
        System.out.println(sol.canPartition(new int[]{2, 2, 3, 5}));
        System.out.println(sol.canPartition(new int[]{3, 3}));        // true
    }
}
