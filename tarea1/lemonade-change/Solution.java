/**
 * 860. Lemonade Change
 * https://leetcode.com/problems/lemonade-change/
 *
 * Enfoque greedy:
 * En cada cliente decidimos LOCALMENTE con qué billetes dar el cambio,
 * priorizando gastar los billetes de $10 antes que los de $5, porque
 * un billete de $5 sirve para dar cambio de $10 O de $20, mientras que
 * uno de $10 solo sirve para dar cambio de $20. Conservar los de $5 el
 * mayor tiempo posible mantiene más opciones abiertas para el futuro.
 * No hay "vuelta atrás": una vez dado el cambio, se sigue con el
 * siguiente cliente.
 *
 * - Cliente paga con $5 -> no se da cambio, solo se guarda el billete.
 * - Cliente paga con $10 -> se necesita dar $5 de cambio.
 *      Si no hay un $5 disponible, es imposible -> false.
 * - Cliente paga con $20 -> se necesita dar $15 de cambio.
 *      Preferencia 1: un billete de $10 + uno de $5 (gasta el $10 que
 *      no sirve para nada más).
 *      Preferencia 2: tres billetes de $5 (si no hay $10 disponible).
 *      Si ninguna combinación es posible -> false.
 *
 * Complejidad:
 *   Tiempo:  O(n)  -> una sola pasada sobre el arreglo bills.
 *   Espacio: O(1)  -> solo se usan dos contadores (five, ten).
 */
class Solution {
    public boolean lemonadeChange(int[] bills) {
        int five = 0, ten = 0;

        for (int bill : bills) {
            if (bill == 5) {
                five++;
            } else if (bill == 10) {
                if (five == 0) {
                    return false;
                }
                five--;
                ten++;
            } else { // bill == 20
                if (ten > 0 && five > 0) {
                    // Preferencia 1: gastar un $10 + un $5
                    ten--;
                    five--;
                } else if (five >= 3) {
                    // Preferencia 2: tres $5
                    five -= 3;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // Método main opcional para probar localmente antes de subir a LeetCode.
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.lemonadeChange(new int[]{5, 5, 5, 10, 20})); // true
        System.out.println(sol.lemonadeChange(new int[]{5, 5, 10, 10, 20})); // false
        System.out.println(sol.lemonadeChange(new int[]{5, 5, 10}));         // true
    }
}
