import java.util.Arrays;

/**
 * 322. Coin Change
 * https://leetcode.com/problems/coin-change/
 *
 * Enfoque: programación dinámica, tabulación bottom-up (mochila NO acotada).
 *
 * Estado:
 *   dp[x] = mínimo número de monedas que suman EXACTAMENTE x.
 *   Mientras x no se pueda armar, dp[x] vale INF = amount + 1 (ninguna
 *   solución real usa más de amount monedas, porque la moneda mínima es 1).
 *
 * Caso base:
 *   dp[0] = 0 -> el monto 0 se arma con cero monedas.
 *
 * Recurrencia:
 *   dp[x] = min over c in coins, c <= x, of  1 + dp[x - c]
 *   "La última moneda que pongo es c; el resto (x - c) lo armo de la mejor
 *   forma posible, que ya está calculada porque x - c < x".
 *
 * Reuso (no acotada):
 *   Hay infinitas monedas de cada tipo. Por eso x se recorre hacia ADELANTE
 *   (1 .. amount): dp[x - c] ya puede contener la misma moneda c, y está
 *   bien que se vuelva a usar.
 *
 * Por qué NO greedy:
 *   Con coins = {1, 3, 4} y amount = 6, "siempre la más grande" toma
 *   4 + 1 + 1 = 3 monedas; la DP encuentra 3 + 3 = 2. El sistema no es
 *   canónico, así que tomar la mayor no garantiza el óptimo.
 *
 * Respuesta:
 *   Si dp[amount] sigue en INF, no hay combinación -> -1.
 *
 * Complejidad (k = coins.length, X = amount):
 *   Tiempo:  Θ(k · X) -> X estados, k transiciones por estado.
 *            Es pseudo-polinomial: depende del VALOR de amount, no del
 *            número de bits con que se escribe.
 *   Espacio: Θ(X) -> el arreglo dp de tamaño X + 1.
 */
class Solution {
    public int coinChange(int[] coins, int amount) {
        int inf = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, inf);
        dp[0] = 0;

        for (int x = 1; x <= amount; x++) {          // hacia adelante: monedas se reusan
            for (int c : coins) {
                if (c <= x && dp[x - c] + 1 < dp[x]) {
                    dp[x] = dp[x - c] + 1;
                }
            }
        }
        return dp[amount] == inf ? -1 : dp[amount];
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.coinChange(new int[]{1, 2, 5}, 11)); // 3 (5+5+1)
        System.out.println(sol.coinChange(new int[]{2}, 3));        // -1
        System.out.println(sol.coinChange(new int[]{1}, 0));        // 0
        System.out.println(sol.coinChange(new int[]{1, 3, 4}, 6));  // 2 (3+3), greedy daría 3
    }
}
