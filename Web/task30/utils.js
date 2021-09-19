const init = function(){
    document.getElementById('id_btn-submit').addEventListener('click', send);
}


const send = function(ev){
    ev.preventDefault();
    ev.stopPropagation();

    let fails = validate();
    if(fails.length === 0){
        saveFormValues("forms.txt");
        document.getElementById('root-form').submit();
    }else{
        fails.forEach(function(obj){
            let field = document.getElementById(obj.input);
        })
    }
}

const validate = function(ev){
    let failures = [];

    let first   = document.getElementById('id_first_name');
    let last    = document.getElementById('id_last_name');
    let email   = document.getElementById('id_email');
    let company = document.getElementById('id_company');
    let city    = document.getElementById('id_city');
    let message = document.getElementById('id_message');

    if(!first.validity.valid){
        failures.push({input:'id_first_name', msg:'Required Field'});
    }
    if(!last.validity.valid){
        failures.push({input:'id_last_name', msg:'Required Field'});
    }
    if(!company.validity.valid ){
        failures.push({input:'id_company', msg:'Required Field'});
    }
    if(!email.validity.valid){
        failures.push({input:'id_email', msg:'Required Field'});
    }
    if(!city.validity.valid){
        failures.push({input:'id_city', msg:'Required Field'});
    }
    if(!message.validity.valid){
        failures.push({input:'id_message', msg:'Required Field'});
    }

    return failures;
}

function saveFormValues(filename) {
    let first   = document.getElementById('id_first_name');
    let last    = document.getElementById('id_last_name');
    let email   = document.getElementById('id_email');
    let company = document.getElementById('id_company');
    let city    = document.getElementById('id_city');
    let message = document.getElementById('id_message');

    const content = "\rFirst name:" + first.value + '\r\n' +
        'Last name: '  + last.value     +       '\r\n'     +
        'Email: '      + email.value    +       '\r\n'     +
        'Company: '    + company.value  +       '\r\n'     +
        'City: '       + city.value     +       '\r\n'     +
        'Message: '    + message.value;


    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(content));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);
    element.click();

    document.body.removeChild(element);
}

document.addEventListener('DOMContentLoaded', init);