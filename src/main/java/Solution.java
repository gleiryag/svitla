// you can also use imports, for example:
// import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Pair<L,R>{
    public L l;
    public R r;

    Pair(L l,R r){
        this.l = l;
        this.r = r;
    }


}

class ProblemFormatting{
    static int digits(int n){
        int i =0;
        while(n!=0){
            n /= 10;
            i++;
        }
        return i;
    }

    static String zeros(int n){
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<n;i++) builder.append("0");
        return builder.toString();
    }

    static String formatNumber(int number,int max){
        return String.format("%s%s",zeros(digits(max)-digits(number)),Integer.toString(number));

    }
}

class OldLabeling{


    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String photo_name;
    String extension;
    String city_name;
    Date date;

    Pattern pattern =  Pattern.compile("(\\w+).(\\w+), (\\w+), (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");

    public  OldLabeling(String s){

        Matcher matcher = pattern.matcher(s);

        if(matcher.matches()){
            photo_name = matcher.group(1);
            extension = matcher.group(2);
            city_name = matcher.group(3);
            try{
                String dateString = matcher.group(4);
                date = formatter.parse(dateString);
            }catch (Exception e){
            }
        }
    }

    public String toString(){
        return String.format("photo_name: %s extension %s city_name: %s date: %s",photo_name,extension,city_name,date);
    }

}


class Solution {

    public String solution(String S) {
        List<OldLabeling> labels = splitbyline(S).stream().map(label->parseLabel(label)).collect(Collectors.toList());


        Map<String, Set<Pair<Integer,OldLabeling>>> sortedByCityLabels = sortByCity(labels);

        List<Set<Pair<Integer,String>>> labelSets =  sortedByCityLabels.entrySet().stream().map(entry->mapToNames(entry.getValue())).collect(Collectors.toList());

        List<Pair<Integer,String>> newLabels = new ArrayList<>();
        labelSets.forEach(newLabels::addAll);

        Collections.sort(newLabels, new Comparator<Pair<Integer, String>>() {
            @Override
            public int compare(Pair<Integer, String> o1, Pair<Integer, String> o2) {
                return Integer.compare(o1.l,o2.l);
            }
        });


        return String.join("\n",newLabels.stream().map(p->p.r).collect(Collectors.toList()));
    }

    private Set<Pair<Integer, String>> mapToNames(Set<Pair<Integer, OldLabeling>> set) {
        int i = 1;
        Set<Pair<Integer, String>> labelWithOrder = new HashSet<>();
        for(Pair<Integer,OldLabeling> p:set){

            String new_name = String.format("%s%s.%s",p.r.city_name,ProblemFormatting.formatNumber(i,set.size()),p.r.extension);

            labelWithOrder.add(new Pair<>(p.l,new_name));

            i++;
        }

        return labelWithOrder;
    }

    private Map<String, Set<Pair<Integer,OldLabeling>>> sortByCity(List<OldLabeling> labels){
        Map<String, Set<Pair<Integer,OldLabeling>>> cities = new HashMap<>() ;
        int i = 0;
        for(OldLabeling label: labels){

            Set<Pair<Integer,OldLabeling>> citySet;
            if(cities.containsKey(label.city_name)){
                citySet = cities.get(label.city_name);
            }
            else{

                citySet = new TreeSet<>(new Comparator<Pair<Integer, OldLabeling>>() {
                    @Override
                    public int compare(Pair<Integer, OldLabeling> o1, Pair<Integer, OldLabeling> o2) {
                        return o1.r.date.compareTo(o2.r.date);
                    }
                });

                cities.put(label.city_name,citySet);

            }
            citySet.add(new Pair(i,label));
            i++;
        }
        return cities;
    }


    private List<String> splitbyline(String s){
        return Arrays.asList(s.split("\\n"));
    }

    private OldLabeling parseLabel(String label){
        return new OldLabeling(label);
    }

}