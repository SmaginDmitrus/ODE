import kotlin.math.*
import jetbrains.letsPlot.*
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.geom.geomPoint

fun f(y0:Double,z0:Double): Pair<Double,Double> {

    val f2 = 0.1*(1-z0*z0)*z0+cos(y0)
    val f1 = z0
    var answer = Pair<Double,Double>(f1,f2)
    return answer
}

fun ode_solution(z0: Double, y0: Double, h: Double): Pair<ArrayList<Double>, ArrayList<Double>> {
//z = y', таким образом мы разбили ОДУ 2ого порядка на систему из двух ОДУ 1ого порядка,z0,y0 - начальные условия, на выходе формируем  массивы значений y и y' и собираем их в пару
    var z = z0
    var y: Double = y0

    var k1 = Array<Double>(2){_->0.0}
    var k2 = Array<Double>(2){_->0.0}
    var k3 = Array<Double>(2){_->0.0}
    var k4 = Array<Double>(2){_->0.0}

    var answer_y = arrayListOf<Double>()
    var answer_z = arrayListOf<Double>()
    answer_y.add(y)
    answer_z.add(z)
    for (i in 1..9999) {
        k1[0] = h*f(y,z).first
        k1[1] = h*f(y,z).second
        k2[0] = h*f(y+(k1[0]/2),z+(k1[1]/2)).first
        k2[1] = h*f(y+(k1[0]/2),z+(k1[1]/2)).second
        k3[0] = h*f(y+(k1[0]/2),z+(k1[1]/2)).first
        k3[1] = h*f(y+(k2[0]/2),z+(k2[1]/2)).second
        k4[0] = h*f(y+(k3[0]),z+(k3[1])).first
        k4[1] = h*f(y+(k3[0]),z+(k3[1])).second
        y += (k1[0]+2*k2[0]+2*k3[0] + k4[0])/6
        print("$y|")
        z += (k1[1]+2*k2[1]+2*k3[1] + k4[1])/6
        println(z)
        answer_y.add(y)
        answer_z.add(z)

    }
    var answer = Pair<ArrayList<Double>, ArrayList<Double>>(answer_y, answer_z)


    return answer
}

fun main() {
    val h: Double = 0.01 //шаг  сетки
    var a = 0.0
    var z0 = 0.0
    var y0: Double = 1.0
    val answer = ode_solution(z0, y0, h)
    val answer_y = answer.first
    val answer_z = answer.second
    var dots = arrayListOf<Double>()
    dots.add(a)
    for (i in 0..9998) {
        dots.add(a)
        a += h
    }
    val data = mapOf<String, Any>("x" to dots, "y" to answer_y)
    val data1 = mapOf<String, Any>("y" to answer_y, "y'" to answer_z)
    val fig1 = letsPlot(data) + geomPoint(color = "blue", size = 1.0) { x = "x";y = "y" } + geomLine(color = "blue")
    val fig2 = letsPlot(data1) + geomPoint(color = "blue", size = 1.0) { x = "y";y = "y'" } + geomLine(color = "blue")
    ggsave(fig1, "y_plot.png")
    ggsave(fig2, "phase_portrait.png")
}